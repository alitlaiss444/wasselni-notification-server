package com.wasselni.common.utils;

import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;

public class KeyStoreUtils {

	private static final Log log = LogFactory.getLog(KeyStoreUtils.class);

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	public static class KeyCert {
		public PrivateKey privateKey;
		public Certificate certificate;
		public CertStore certs;

		public KeyCert(PrivateKey privateKey, Certificate certificate) {
			super();
			this.privateKey = privateKey;
			this.certificate = certificate;
		}

		public KeyCert(PrivateKey privateKey, Certificate certificate, CertStore certs) {
			super();
			this.privateKey = privateKey;
			this.certificate = certificate;
			this.certs = certs;
		}
	}

	/**
	 * Retreive private key from keystore
	 * 
	 * @param keystore
	 * @param keystorePassphrase
	 * @param privateKeyName
	 * @param passphrase
	 * @return
	 * @throws SystemException
	 */
	public static PrivateKey getPrivateKey(String keystore, String keystorePassphrase, String privateKeyName,
			String passphrase) throws SystemException {
		try {

			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(new FileInputStream(keystore), keystorePassphrase.toCharArray());
			return (PrivateKey) keyStore.getKey(privateKeyName, passphrase.toCharArray());
		} catch (Exception e) {
			log.error("Error getting private key", e);
			throw new SystemException(SystemError.UNKNOWN_ERROR);
		}
	}

	/**
	 * read keystore
	 * 
	 * @param keystore
	 * @param keystorePassphrase
	 * @return
	 * @throws SystemException
	 */
	public static KeyStore getKeystore(String keystore, String keystorePassphrase) throws SystemException {

		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(new FileInputStream(keystore), keystorePassphrase.toCharArray());
			return keyStore;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error loading keystore", e);
			throw new SystemException(SystemError.UNKNOWN_ERROR);
		}
	}

	/**
	 * Read private key and certificate from keystore
	 * 
	 * @param keystore
	 * @param keystorePassphrase
	 * @param privateKeyName
	 * @param passphrase
	 * @return
	 * @throws SystemException
	 */
	public static KeyCert getKeyCert(String keystore, String keystorePassphrase, String privateKeyName,
			String passphrase) throws SystemException {
		return getKeyCert(keystore, keystorePassphrase, privateKeyName, passphrase, false);
	}

	/**
	 * Read private key and certificate from keystore
	 * 
	 * @param keystore
	 * @param keystorePassphrase
	 * @param privateKeyName
	 * @param passphrase
	 * @return
	 * @throws SystemException
	 */
	public static KeyCert getKeyCert(String keystore, String keystorePassphrase, String privateKeyName,
			String passphrase, boolean returnCertStore) throws SystemException {
		try {

			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(new FileInputStream(keystore), keystorePassphrase.toCharArray());

			PrivateKey privateKey = (PrivateKey) keyStore.getKey(privateKeyName, passphrase.toCharArray());

			if (privateKey == null)
				throw new SystemException(SystemError.VE_MISSING_MANDATORY, "Private Key");

			Certificate cert = keyStore.getCertificate(privateKeyName);

			CertStore certs = null;

			if (returnCertStore) {
				java.security.cert.Certificate[] certChain = keyStore.getCertificateChain(privateKeyName);
				List<Certificate> certList = new ArrayList<>();
				for (int i = 0; i < certChain.length; i++)
					certList.add(certChain[i]);

				certs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
			}

			return new KeyCert(privateKey, cert, certs);

		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error getting key pair", e);
			throw new SystemException(SystemError.UNKNOWN_ERROR);
		}
	}

	/**
	 * Read public key from keystore
	 * 
	 * @param keystore
	 * @param keystorePassphrase
	 * @param publicKeyName
	 * @return
	 * @throws SystemException
	 */
	public static PublicKey getPublicKey(String keystore, String keystorePassphrase, String publicKeyName)
			throws SystemException {
		try {

			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(new FileInputStream(keystore), keystorePassphrase.toCharArray());
			return keyStore.getCertificate(publicKeyName).getPublicKey();
		} catch (Exception e) {
			log.error("Error getting private key", e);
			throw new SystemException(SystemError.UNKNOWN_ERROR);
		}
	}

	public static KeyCert getRandomKey(String cryptoName) throws SystemException {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(cryptoName);
			SecureRandom secRan = new SecureRandom();
			kpg.initialize(512, secRan);
			KeyPair keyP = kpg.generateKeyPair();
			return new KeyCert(keyP.getPrivate(), null);
		} catch (Exception e) {
			log.error("Error getting random key", e);
			throw new SystemException(SystemError.UNKNOWN_ERROR);
		}
	}
}
