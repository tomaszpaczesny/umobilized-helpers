package com.umobilized.helpers.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import com.umobilized.helpers.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

/**
 * Helper class to use KeyStore and Cipher features to achieve secure secret store.
 *
 * Strongly inspired by:
 * https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
 * https://proandroiddev.com/secure-data-in-android-encryption-in-android-part-2-991a89e55a23
 *
 * Created by tpaczesny on 2018-01-09.
 */

public class SecretManager {
    private static final String SECRET_FILES_DIR = "secret_files";

    private static final String SHARED_PREF_KEY = "com.umobilzied.helpers.secret_key";
    private static final String SHARED_PREF_SECRETS = "com.umobilzied.helpers.secret_store";
    private static final String SHARED_PREF_FILES = "com.umobilzied.helpers.secret_files";

    private static final String KEY_ENCRYPTED_KEY = "key";
    private static final String RSA_MODE =  "RSA/ECB/PKCS1Padding";
    private static final String AES_MODE = "AES/ECB/PKCS7Padding";
    private static final String KEY_ALIAS =  "MASTER";

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private static final int BUFFER_SIZE = 1024;

    private static SecretManager sInstance;

    public static SecretManager getInstance() {
        if (sInstance == null)
            sInstance = new SecretManager();
        return sInstance;
    }

    private KeyStore mKeyStore;
    private boolean mSecurityReady;

    private SecretManager() {
        mSecurityReady = false;
    }

    public void init(Context context) throws Exception {
        mKeyStore = createAndroidKeyStore();
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        String encryptedKeyB64 = pref.getString(KEY_ENCRYPTED_KEY, null);
        if (!mKeyStore.containsAlias(KEY_ALIAS) || encryptedKeyB64 == null) {
            // clear secret store, as it may contain unreadable data now
            context.getSharedPreferences(SHARED_PREF_SECRETS, Context.MODE_PRIVATE).edit().clear().apply();

            createAsymmetricKey(context, KEY_ALIAS);

            byte[] key = new byte[16];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(key);
            byte[] encryptedKey = rsaEncrypt(key, KEY_ALIAS);
            String enryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(KEY_ENCRYPTED_KEY, enryptedKeyB64);
            edit.apply();
        }
        mSecurityReady = true;
    }

    public boolean isSecurityReady() {
        return mSecurityReady;
    }

    public void storeStringSecret(Context context, String key, String value) throws Exception {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_SECRETS, Context.MODE_PRIVATE).edit();
        if (value != null) {
            editor.putString(key, encrypt(context, value.getBytes())).apply();
        } else {
            editor.remove(key).apply();
        }
    }

    public String getStringSecret(Context context, String key) throws Exception {
        String encryptedB64 = context.getSharedPreferences(SHARED_PREF_SECRETS, Context.MODE_PRIVATE)
                .getString(key, null);
        return encryptedB64 != null ? new String(decrypt(context, encryptedB64)) : null;
    }

    public boolean hasSecret(Context context, String key) {
        return context.getSharedPreferences(SHARED_PREF_SECRETS, Context.MODE_PRIVATE).contains(key);
    }

    public Set<String> getSecretEntryKeys(Context context) {
        return context.getSharedPreferences(SHARED_PREF_SECRETS, Context.MODE_PRIVATE).getAll().keySet();
    }

    /**
     * Stores encrypted version of file in the device storage.
     * Make sure to disposed plain file after encryption is done.
     * @param context
     * @param value
     * @throws Exception
     */
    public void storeFileSecret(Context context, String key, File value) throws Exception {
        BufferedOutputStream outputStream = null;
        BufferedInputStream inputStream = null;
        try {
            File dir = new File(context.getFilesDir(), SECRET_FILES_DIR);
            if (!dir.exists()) dir.mkdir();

            File out = new File(dir, UUID.randomUUID().toString());
            outputStream = new BufferedOutputStream(new FileOutputStream(out));

            outputStream.write(encryptToBytes(context, FileUtils.readBytes(value)));

            context.getSharedPreferences(SHARED_PREF_FILES, Context.MODE_PRIVATE)
                    .edit()
                    .putString(key, out.getAbsolutePath())
                    .apply();

        } catch (IOException e) {
            throw e;
        } finally {
            FileUtils.closeQuietly(inputStream);
            FileUtils.closeQuietly(outputStream);
        }
    }

    public File getFileSecret(Context context, String filenameKey, boolean deleteSecret) throws Exception {
        BufferedOutputStream outputStream = null;
        BufferedInputStream inputStream = null;

        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_FILES, Context.MODE_PRIVATE);
        File secretFile;
        try {
            if (pref.contains(filenameKey) && (secretFile = new File(pref.getString(filenameKey, ""))).exists()) {
                File out = FileUtils.getInternalCacheFile(context, SECRET_FILES_DIR, filenameKey);
                outputStream = new BufferedOutputStream(new FileOutputStream(out));

                outputStream.write(decrypt(context, FileUtils.readBytes(secretFile)));

                if (deleteSecret) {
                    secretFile.delete();
                    pref.edit().remove(filenameKey).apply();
                }

                return out;
            }
        } catch (IOException e) {
            throw e;
        } finally {
            FileUtils.closeQuietly(inputStream);
            FileUtils.closeQuietly(outputStream);
        }
        return null;
    }

    public void removeFileSecret(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_FILES, Context.MODE_PRIVATE);

        File secretFile;
        if (pref.contains(key)) {
            secretFile = new File(pref.getString(key, ""));
            if (secretFile.exists()) {
                secretFile.delete();
            }
            pref.edit().remove(key).apply();
        }
    }

    public boolean hasFileSecret(Context context, String filenameKey) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_FILES, Context.MODE_PRIVATE);
        return pref.contains(filenameKey) && (new File(pref.getString(filenameKey, ""))).exists();
    }

    public Set<String> getSecretFileEntryKeys(Context context) {
        return context.getSharedPreferences(SHARED_PREF_FILES, Context.MODE_PRIVATE).getAll().keySet();
    }

    public String encrypt(Context context, byte[] input) throws Exception {
        Cipher c = Cipher.getInstance(AES_MODE, "BC");
        c.init(Cipher.ENCRYPT_MODE, getSecretKey(context, KEY_ALIAS));
        byte[] encodedBytes = c.doFinal(input);
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    }

    public byte[] encryptToBytes(Context context, byte[] input) throws Exception {
        Cipher c = Cipher.getInstance(AES_MODE, "BC");
        c.init(Cipher.ENCRYPT_MODE, getSecretKey(context, KEY_ALIAS));
        return c.doFinal(input);
    }


    public byte[] decrypt(Context context, byte[] encrypted) throws Exception {
        Cipher c = Cipher.getInstance(AES_MODE, "BC");
        c.init(Cipher.DECRYPT_MODE, getSecretKey(context, KEY_ALIAS));
        return c.doFinal(encrypted);
    }

    public byte[] decrypt(Context context, String encryptedB64) throws Exception {
        byte[] encrypted = Base64.decode(encryptedB64, Base64.DEFAULT);
        Cipher c = Cipher.getInstance(AES_MODE, "BC");
        c.init(Cipher.DECRYPT_MODE, getSecretKey(context, KEY_ALIAS));
        return c.doFinal(encrypted);
    }

    private KeyStore createAndroidKeyStore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore ks = KeyStore.getInstance(ANDROID_KEY_STORE);
        ks.load(null);
        return ks;
    }

    private KeyPair createAsymmetricKey(Context context, String alias) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initGeneratorWithKeyGenParameterSpec(generator, alias);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            initGeneratorWithKeyPairGeneratorSpec(context, generator, alias);
        } else {
            return null;
        }

        return generator.generateKeyPair();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initGeneratorWithKeyPairGeneratorSpec(Context context, KeyPairGenerator generator, String alias) throws Exception {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 20);

        generator.initialize(new KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSerialNumber(BigInteger.ONE)
                .setSubject(new X500Principal("CN=" + alias + " CA Certificate"))
                .setStartDate(startDate.getTime())
                .setEndDate(endDate.getTime())
                .build());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initGeneratorWithKeyGenParameterSpec(KeyPairGenerator generator, String alias) throws InvalidAlgorithmParameterException {
        generator.initialize(new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build());
    }

    private byte[] rsaEncrypt(byte[] secret, String alias) throws Exception{
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(alias, null);
        // Encrypt the text
        Cipher inputCipher = Cipher.getInstance(RSA_MODE);
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();

        return outputStream.toByteArray();
    }

    private  byte[]  rsaDecrypt(byte[] encrypted, String alias) throws Exception {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)mKeyStore.getEntry(alias, null);
        Cipher output = Cipher.getInstance(RSA_MODE);
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(encrypted), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte)nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i).byteValue();
        }
        return bytes;
    }


    private Key getSecretKey(Context context, String alias) throws Exception{
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String enryptedKeyB64 = pref.getString(KEY_ENCRYPTED_KEY, null);
        // need to check null, omitted here
        byte[] encryptedKey = Base64.decode(enryptedKeyB64, Base64.DEFAULT);
        byte[] key = rsaDecrypt(encryptedKey, alias);
        return new SecretKeySpec(key, "AES");
    }
}
