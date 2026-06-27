package com.wasselni.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Hex;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;

public class RSAUtils {

	private static final Log log = LogFactory.getLog(RSAUtils.class);

	private final static String cryptoName = "RSA";
	private final static String cryptoProvider = "BC";
	private final static String cryptoAlgorythm = "RSA/ECB/PKCS1Padding";
	private final static String signatureCryptoName = "SHA256withRSA";

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	public static PublicKey publicKeyFromCertificate(String cert) throws CertificateException {
		CertificateFactory f = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) f
				.generateCertificate(new ByteArrayInputStream(cert.getBytes()));
		return certificate.getPublicKey();

	}

	public enum KeyType {
		PublicKey, PrivateKey, Certificate
	}

	public static byte[] readFromFile(String fileName, final KeyType keyType) throws SystemException {
		if (StringUtils.isBlank(fileName) || keyType == null) {
			return null;
		}
		String string = null;

		if (keyType == KeyType.PrivateKey) {
			string = "PRIVATE KEY";
		} else if (keyType == KeyType.PublicKey) {
			string = "PUBLIC KEY";
		} else if (keyType == KeyType.Certificate) {
			string = "CERTIFICATE";
		}

		String str = null;
		File file = new File(fileName);
		try (InputStream is = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			StringBuilder builder = new StringBuilder();
			boolean inKey = false;
			boolean isPEM = false;
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (!inKey) {
					if (line.contains(string)) {
						inKey = true;
						if (!isPEM)
							isPEM = true;
					}
					continue;
				} else {
					if (line.contains(string)) {
						inKey = false;
						break;
					}
					builder.append(line);
				}
			}

			str = builder.toString();
		} catch (IOException e) {
			log.error("IO Error", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR, e.getMessage());
		}

		if (StringUtils.isBlank(str)) {
			return FileUtils.readFile(keyType.name(), fileName).getBytes();
		}

		return Base64.decodeBase64(str);
	}

	/**
	 * 
	 * @param privateKeyInHex
	 * @return
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static RSAPrivateKey getPrivateKeyFromHexString(String privateKeyInHex)
			throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		byte[] bytes = Hex.decode(privateKeyInHex.getBytes());
		KeyFactory factory = KeyFactory.getInstance(cryptoName, cryptoProvider);
		RSAPrivateKey key = (RSAPrivateKey) factory.generatePrivate(new PKCS8EncodedKeySpec(bytes));

		return key;
	}

	public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws SystemException {
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(cryptoName);
			PublicKey pk = keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(cryptoAlgorythm);
			cipher.init(Cipher.ENCRYPT_MODE, pk);
			return cipher.doFinal(data);
			// ByteArrayOutputStream baos = new ByteArrayOutputStream(245);
			// for (int i = 0; i < data.length; i += 245) {
			// baos.write(cipher.doFinal(ArrayUtils.subarray(data, i, 245)));
			// }
			// return baos.toByteArray();
		} catch (Exception e) {
			log.error("RSA Error", e);
			e.printStackTrace();
			throw new SystemException(SystemError.SE_ENCRYPTION_ERROR, "RSA error");
		}
	}

	public static byte[] decryptByPrivateKey(byte[] data, PrivateKey pk) throws SystemException {
		return decryptByPrivateKey(data, pk, cryptoAlgorythm);
	}

	public static byte[] decryptByPrivateKey(byte[] data, PrivateKey pk, String algorithm) throws SystemException {
		try {
			Cipher cipher;
			if (algorithm.equals("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")) {
				cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
				OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1",
						new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
				cipher.init(Cipher.DECRYPT_MODE, pk, oaepParams);
			} else {
				cipher = Cipher.getInstance(algorithm);
				cipher.init(Cipher.DECRYPT_MODE, pk);
			}

			return cipher.doFinal(data);
		} catch (Exception e) {
			log.error("Decryption Error", e);
			e.printStackTrace();
			throw new SystemException(SystemError.SE_ENCRYPTION_ERROR, "RSA error");
		}
	}

	public static byte[] signMessage(byte[] message, PrivateKey privateKey) throws SystemException {
		try {
			Signature sig = Signature.getInstance(signatureCryptoName);
			sig.initSign(privateKey);
			sig.update(message);
			byte[] sign = sig.sign();
			return sign;
		} catch (Exception e) {
			log.error("Cannot sign message", e);
			throw new SystemException(SystemError.SE_ENCRYPTION_ERROR, e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static byte[] signMessage2(byte[] message, PrivateKey privateKey, X509Certificate cert, CertStore certs)
			throws SystemException {
		try {
			CMSSignedDataGenerator sgen = new CMSSignedDataGenerator();

			ContentSigner contentSigner = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC")
					.build(privateKey);

			sgen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
					new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(contentSigner, cert));

			Collection<? extends Certificate> certCollection = certs.getCertificates(null);

			Store<X509CertificateHolder> certStore = new JcaCertStore(certCollection);

			sgen.addCertificates(certStore);

			CMSTypedData cmsData = new CMSProcessableByteArray(message);

			CMSSignedData signedData = sgen.generate(cmsData, true);

			return signedData.getEncoded();

		} catch (Exception e) {
			log.error("Cannot sign message", e);
			throw new SystemException(SystemError.SE_ENCRYPTION_ERROR, e.getMessage());
		}
	}

	public static boolean validateMessageSignature(PublicKey publicKey, byte[] message, byte[] signature)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature clientSig = Signature.getInstance(signatureCryptoName);
		clientSig.initVerify(publicKey);
		clientSig.update(message);
		return clientSig.verify(signature);
	}

	public static void mainw(String[] args)
			throws SystemException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

		PrivateKey key = KeyStoreUtils.getPrivateKey("/wallet.cms.encryption.key@2022.p12", "key@2022",
				"wallet.cms.encryption", "key@2022");

		byte[] pubkey = readFromFile("/CardActivationNFCPublikKey.pem", KeyType.PublicKey);

		byte[] data = RSAUtils.encryptByPublicKey("0001000000000028,2024-10-30".getBytes(), pubkey);

		String str = Base64.encodeBase64String(data);

		log.debug(str);
		System.out.println(str);

		// String
		// str =
		// "Mi71l0nF1ILS64qrgFtEb+N3eDGRf6tkri/QwAdaGpR9NGIr0dJ4v8pseIjo9J5yeBZGxT3YmHcM+KlgZmes0UqVjPPcTXKPcF7KjJcCO4scHTdthV+2lMKhSrTlf+4VZ/Cfr5x0ry3RgtBvfrVxKlEOBn+E7FmQwsC0/w1BwvY3dnGwtJNm7KiuZDQOGVOZiFNgQxDwvoQadPvsmTHwECP1Tpe0j0odJGXeDy9KxpUJ9Jjxu8NZowK5Sw2/PotPi0Z9GRKrXVMNWB3H7KVKu0oScr3Hc7yd3ihM2bSTmpkEh+FAGUmDgdBGoQfUGA8wuVKogxQZ79jjYrmIWA0bfA==";

		byte[] x = RSAUtils.decryptByPrivateKey(Base64.decodeBase64(str), key);

		String out = new String(x);

		log.debug(out);

		System.out.println(out);

	}

	public static PrivateKey getPrivateKeyFromFile(String path) throws SystemException {
		return getPrivateKey(readFromFile(path, KeyType.PrivateKey));
	}

	public static PrivateKey getPrivateKey(String base64PrivateKey) {
		return getPrivateKey(Base64.decodeBase64(base64PrivateKey.getBytes()));
	}

	public static PrivateKey getPrivateKey(byte[] privateKeyBytes) {
		PrivateKey privateKey = null;
		try {

			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
			KeyFactory keyFactory = null;
			keyFactory = KeyFactory.getInstance(cryptoName);
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return privateKey;
	}

	public static PublicKey getPublicKey(String b64PublicKey) {
		return getPublicKey(Base64.decodeBase64(b64PublicKey.getBytes()));
	}

	public static RSAPublicKey getRSAPublicKey(String b64PublicKey) {
		return getRSAPublicKey(Base64.decodeBase64(b64PublicKey.getBytes()));
	}

	public static PublicKey getPublicKeyFromFile(String path) throws SystemException {
		return getPublicKey(readFromFile(path, KeyType.PublicKey));
	}

	public static RSAPublicKey getRSAPublicKeyFromCertFile(String path) throws SystemException {
		return getRSAPublicKey(readFromFile(path, KeyType.PublicKey));
	}

	public static PublicKey getPublicKey(byte[] publicKeyBytes) {
		PublicKey publicKey = null;

		try {
			KeyFactory factory = KeyFactory.getInstance(cryptoName);
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			return factory.generatePublic(pubKeySpec);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	public static RSAPublicKey getRSAPublicKey(byte[] certBytes) {
		RSAPublicKey publicKey = null;
		try {

			Certificate cf = CertificateFactory.getInstance("X.509")
					.generateCertificate(new ByteArrayInputStream(certBytes));

			publicKey = (RSAPublicKey) cf.getPublicKey();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return publicKey;
	}

	public static X509Certificate getCertificateFromFile(String path) throws SystemException {
		return getCertificate(readFromFile(path, KeyType.Certificate));
	}

	public static X509Certificate getCertificate(String b64Certificate) {
		return getCertificate(Base64.decodeBase64(b64Certificate.getBytes()));
	}

	public static X509Certificate getCertificate(byte[] certificateBytes) {
		CertificateFactory certFactory;
		try {
			certFactory = CertificateFactory.getInstance("X.509");

			ByteArrayInputStream certStream = new ByteArrayInputStream(certificateBytes);
			X509Certificate cert = (X509Certificate) certFactory.generateCertificate(certStream);
			return cert;
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3i2Jviwe3rwvLC/Muy2Aqq4FUiATtKM27JUXzZVFbRXrCLndomSPJVWKFF9BT93huNKdwjlTGj03hjJGVjJq8pveaTDZL0tlNQsIJuaNoxFgsjMXFnqMy/FHClTtGvq9/awD7qR8JpKKUQSA8M/r4wosGEc9UGdIUNVdSkVTvYBWnv2CSke9JPLXVPXmXWKraNW2Nis7gGGhlCoHL35tgS2IJs5NF4k/85Ea1sOL9EHSmN9gDMmuL/E9ups99km1llMKndjGqHF7F+h5ip2vP4zz3wDu2vFP00oLTtjHCol1vsP7FJyPdONXfWRMjDb1qqqzpvAccBqUDjS5uciNcwIDAQAB";/// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
	private static String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDeLYm+LB7evC8sL8y7LYCqrgVSIBO0ozbslRfNlUVtFesIud2iZI8lVYoUX0FP3eG40p3COVMaPTeGMkZWMmrym95pMNkvS2U1Cwgm5o2jEWCyMxcWeozL8UcKVO0a+r39rAPupHwmkopRBIDwz+vjCiwYRz1QZ0hQ1V1KRVO9gFae/YJKR70k8tdU9eZdYqto1bY2KzuAYaGUKgcvfm2BLYgmzk0XiT/zkRrWw4v0QdKY32AMya4v8T26mz32SbWWUwqd2MaocXsX6HmKna8/jPPfAO7a8U/TSgtO2McKiXW+w/sUnI9041d9ZEyMNvWqqrOm8BxwGpQONLm5yI1zAgMBAAECggEAWm9GxfXHivomaWH7f6UB3nd/CvKrkrdsv9fgHHpKDCEDzkV5hYRRD+1Fiw1K1GuIWWbBeDFFNkT8uM71+lgDWEVBvqd3f9y8kV6wLsVhmaKVLXPGTgwMC2dmdzoiWPz/IKZH43doYXr8egXPRjYb2gpl8Gvu9S9VLDHtCzI058j3J2guVQOl1Ig/SrLaZzFFDOBKURC2+fluQvg4pHUigqTCiviv0Ufjr0tfADNDSnmxismyU2iPWKzxG+3ad2s+BtvLoUrGAI1Im+IYYw5oflsWn9ZdFzsacRBL/zmphv2Y2xdSTO3i4q2EqDV1Nzwg8Pdfo4WAF/DApcEgHxFWIQKBgQD4ndvg3v80AJyzqCx0y1oZuuXkGRI2/nZ/Z1Wv4qeSrVNyWLfa1TT3gdpm6NF9JUvqjen4CbSH7ZnzpFZf4uANbxuqUQ4NrCaFU9RGi1C2Kbg/Vegk7ohh4bGFQ0kjlHNh3uwD14uSn3tLTYgj2z2gpxE4qCHV8FUISAeW7jGrbQKBgQDkxqzNtKjR/3cnSxxi7O+djwXS8fXkvGpry/TR3KgcIA5lCZggtQf2Ew+81tp+TgK6NGKRJt6dNQ5z9VfiXOgWnAwq2v207ErfQp0LcrPGyqK3bMIQihmbVZEHLqFnfsUfMRs0p5ZYJlwdouJdKLolZ3p1UyFPKSAEec70pxuwXwKBgE8jCIN62Cc4bqjtRkFjHWoFSPojVN32f8esH+Vds1OIIoDKZrOcnECQr/GD+HLqdVDTHX+br0B5TED2tZvN/t2n2VmMdXm6q4RTG8XcwLNLcl+Ou9dNqlHl4nj14wkVTuj/aYcLjFz+sONWaZhoSCSkKWIseqC74IVDUE8lwN+9AoGAf5IuEDM6pIdhKT8gICXhZCm55KxaIufSLDQ6W8BA2hYiEMNt2mc4fkaY0wO4KRBVQwItKKXVIzLP0WexTx/hp6Hywv8E1JujEKYzrouSpjqey8vQ16o6pzzqt2eQR4cQYtleQ1lkY9lYKoqG0c4BJxRx2/S0MVFMclJ+TjzVGt8CgYBsdui9UBTAiFIBluGaqDLyez7DjSjG8oSdWu6hRib0jkMo8JM66aQqfyMRMDKcRf1buNWvbObtDhxCJ/YhkIlLUU0WYAFYmn1LhqDDpADRepbmOItY3ydX/wxmzaRp4xESpQxpSAeiEpQUXX0/G4FRZAIgcRVFH7KoKFurcYl65w==";// "MIICWwIBAAKBgG7cnPB7V2vykv09azXKR9G+rjFTsvKC1pLAYna4diKM9VB4mga2TLY3bBmvBMqcSOkeG1G8OAWe0tHkhsZtISSmJSg1B4qVkyOryB8lfw8aKYsQ3eJJ5fLjRy8qQkF6vn/Rj29sWnXVfqDXLOzElQIXXm+S9/5KVnH4YBPs7xTNAgMBAAECgYApWdacCJWiKRfUp50esjW5sXMN6RddR5o7oNDZUZKW7pbuDh39lO/mI6QJrHOQE11KvrUX9qm6xvQB4Ei5KKlsoNXQsiCoMZ12aw6A2NjOsMNEhj2dGYtBtYppvqOg/xnCst7zrIivxxxA83xC8Y/xT+29piWfJ/mdSG5wPFniAQJBALn7G0kMXamM4BUjxOMrBQvfi7XejQKN4iedJ6nsfjVAfIiIzfuC4GgLLbBeTRxsfZB6VFnLIGKN24aNSKKPJ00CQQCYmYDtGReOJGdLUHABvNJF+3C5rX9dsEXB5N8I+eQ10/PCJ4Td42zBKEG2tRZVPQDQ1jQLWEWpG6x3wPaDP+OBAkBXQ+LuqLW+G9L9OFZm/UWV56Onh7CElUybp9r3mXcyx3pnmkGElUl0qGIc0nK+No3IYA36QByHAecSwHJnjGI1AkEAiF2clcQ3hYFIgUgXX6Yh7gHc4UCGse1k2Pkyym2tHuESUm1IUZT7Sb00xBYdJ/zErtrMVVsDnTQqsjVQ1UcRgQJAeJeRrA2+FabwLvOsvFzNS2oz+87px4fEcw9BEF1BwvzYoGGwex2cPH2XYQA/jAFCy7fLxL2cwGuuxf6wIPRKKw==";
	private static String certificate = "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";

	public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException,
			NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, SignatureException {
		try {

			PrivateKey pk = getPrivateKeyFromFile("D:\\ARB\\private-vdp-key-test.key");

			String pp = "qSp+/gWvqHWItuL4eUUY6/Xcja1wPTYd8t31NLGYb3Exny9/WtM0rwG2QM364LfxXvEyFw9LK7UiFnuWCwngj45btEthM75+qzgYLiLMiUwOEw807yjtHBp2btZtSyvgd4QbpHpppl/cXqq3dRdilPl3xBSwWfeZfXK7LXjvSLGDPkXqVkxvoUc0akouoqpONxd2H6FBvcvLwth8cIk7bzCHRh99O+4LjPXFgOjc1YQVW91pflwQYmCWbGWRuahKulDzYrB5jmHQAjYrLHPaCWAdN2TPrLlh706y1zF+1mQzQ+QtCETWIsXQAFAEQZnnjI9wlGSgYZfYltqNmJQOXQ==";

			byte[] out = decryptByPrivateKey(Base64.decodeBase64(pp), pk);

			String key = new String(out);

			System.out.println(key);

			// String encryptedString = java.util.Base64.getEncoder()
			// .encodeToString(encryptByPublicKey("TONY".getBytes(),
			// Base64.decodeBase64(publicKey)));
			// String encryptedString =
			// "LXnA2d+j1TkGIjltMcnP3xRlVu38qLtR9WTuD8DmDGpPRcl8ftC2r0c90IxaVyC7ZP/RcbC/ehwU1MyblqfMMs+jPUZ9BvnCEXysPEid7MWa56xHyNBBt4PsoggQitzdKbNr/UrMyp2EQC2B3tms+x2maPYsE+ZH4lq0qq+mX+M=";

			// encryptedString =
			// "aC+1fwXwBqxs/U0Ozco+7FloVNCJFDLZgN9xRMbfcAkg0gYiG1nCRkppeGba5fIRJvc4qMCGmAIfMgpxYR6Pyn/IyhS4xT8hSaZRwJcpYBAhydJ8IIdmq/XxaRMGSkCRchrQtSNKEChY4pRQQB3Ulrosf45h62Wd9olXaHfT1dU=";
			// System.out.println(encryptedString);
			// byte[] decryptedString = RSAUtils.decryptByPrivateKey(
			// Base64.decodeBase64(encryptedString),
			// getPrivateKey(privateKey));
			// System.out.println(new String(decryptedString));

			byte[] decryptedString;
			String msg = "hello";
			decryptedString = msg.getBytes();

			byte[] signature = signMessage(msg.getBytes(), getPrivateKey(privateKey));

			System.out.println(Base64.encodeBase64String(signature));

			System.out.println("***********************" + signature.length);

			System.out.println(validateMessageSignature(getPublicKey(publicKey), decryptedString, signature));

			System.out.println("11111111111111111111111");

			signature = signMessage2(msg.getBytes(), getPrivateKey(privateKey), getCertificate(certificate), null);

			System.out.println("*********************** " + signature.length);

			System.out.println(Base64.encodeBase64String(signature));

			System.out.println(validateMessageSignature(getPublicKey(publicKey), decryptedString, signature));

			System.out.println("11111111111111111111111");

			// CVnxg8q35j33OcZK8qg3LMMOMEYzoB6V
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
