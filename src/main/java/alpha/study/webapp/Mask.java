package alpha.study.webapp;

import java.util.Random;

public class Mask {
	static class Cipher {
		int index = 0;
		long id = 0;
		long key = 0;
		long deactive = 0;
		long destroy = 0;
	}

	static final int CIPHERMASK = 0xFFFFF; // 密码掩码，20位
	static final int IDMASK = 0x3FF; // ID掩码，10位
	static final int INDEXMASK = 0x1; // 索引掩码，1位
	static final Random RANDOM = new Random();
	static final long LIFECYCLE = 10 * 1000; // ms
	static final Cipher[] ciphers = new Cipher[2];

	static Cipher getCipher(long now) {
		for (int i = 0; i < ciphers.length; i++) {
			Cipher cipher = ciphers[i];
			// 初始化
			if (cipher == null) {
				cipher = new Cipher();
				cipher.index = (i & INDEXMASK);
				cipher.id = buildlId();
				cipher.key = buildKey();
				cipher.deactive = now + LIFECYCLE;
				cipher.destroy = cipher.deactive + LIFECYCLE;
				ciphers[i] = cipher;
				return cipher;
			}
			// 当前密码还在有效期，继续使用当前密码
			if (now < cipher.deactive) {
				return cipher;
			}
			// 当前密码已经达到/超过销毁时间，销毁并创建新的密码
			if (now >= cipher.destroy) {
				cipher.id = buildlId();
				cipher.key = buildKey();
				cipher.deactive = now + LIFECYCLE;
				cipher.destroy = cipher.deactive + LIFECYCLE;
				ciphers[i] = cipher;
				return cipher;
			}
			// 当前密码已经超过有效期，但未到销毁时间，则保留用于验证之前使用该密码加密的数据
			// 此时，如果同时存在两个密码，那么另外一个密码必然已经达到或者超过销毁时间
		}
		return null;
	}

	public static long encodeTime() {
		long now = System.currentTimeMillis();
		Cipher cipher = getCipher(now);

		if (cipher != null) {
			now = now & CIPHERMASK ^ cipher.key; // 加密时间
			return build(now, cipher.id, cipher.index);
		}
		return 0;
	}
	
	public static long decodeTime(long data) {
		long now = System.currentTimeMillis();
		Cipher cipher = ciphers[decodeIndex(data)];
		if (cipher == null || now >= cipher.destroy) {
			return 0;
		}

		long id = decodeId(data);
		if (cipher.id != id) return 0;
		long key = decodeKey(data);
		return key & CIPHERMASK ^ cipher.key;
	}

	// 生成随机密码（20位）
	static int buildKey() {
		return (RANDOM.nextInt(0xFFFF) + 0xF0000) & CIPHERMASK;
	}

	// 生成随机ID（10位）
	static int buildlId() {
		return ((RANDOM.nextInt(0x300) + 0xFF) & IDMASK);
	}

	static long decodeKey(long data) {
		return data & CIPHERMASK;
	}

	static long decodeId(long data) {
		return (data >> 20) & IDMASK;
	}

	static int decodeIndex(long data) {
		return (int)(data >> 30) & INDEXMASK;
	}

	static long build(long key, long id, long index) {
		return (key & CIPHERMASK) | ((id & IDMASK) << 20) | ((index & INDEXMASK) << 30);
	}
}
