package com.checkhelper;

import java.math.BigInteger;

public final class ChineseCheckConverter {
	public String convertNumberString(String number) {
		if (number.equals ("")) {
			return "請輸入幣值";
		}

		String[] twoParts = number.split ("\\.");
		String integerPart = twoParts[0];
		String decimalPart;
		try {
			decimalPart = twoParts[1];
		} catch (ArrayIndexOutOfBoundsException ex) {
			decimalPart = "";
		}

		if (new BigInteger (integerPart).compareTo (new BigInteger ("9999999999999999")) > 0) {
			return "輸入的幣值過大，必須小於或等於 9999999999999999";
		}

		if (new BigInteger (integerPart).compareTo (new BigInteger ("1")) < 0) {
			return "輸入的幣值過小，必須大於或等於 1";
		}

		String integerString;
		String decimalString = "";

		integerString = convertInteger (integerPart);

		if (decimalPart.equals ("") || Integer.parseInt (decimalPart) == 0) {
			decimalString = "";
		} else {
			if (decimalPart.length () < 2) {
				decimalPart += "0";
			}
			String jiao = decimalPart.substring (0, 1);
			String fen = decimalPart.substring (1, 2);
			if (!jiao.equals ("0")) {
				decimalString += convertNumber (getDigitFromString (jiao, 0)) + "角";
			}

			if (!fen.equals ("0")) {
				decimalString += convertNumber (getDigitFromString (fen, 0)) + "分";
			}
		}

		return integerString + "圓" + decimalString + "正";
	}

	private String convertNumber (int i) {
		switch (i) {
			case 0:
				return "零";
			case 1:
				return "壹";
			case 2:
				return "貳";
			case 3:
				return "叁";
			case 4:
				return "肆";
			case 5:
				return "伍";
			case 6:
				return "陸";
			case 7:
				return "柒";
			case 8:
				return "捌";
			case 9:
				return "玖";
			default:
				return "";
		}
	}

	private String getThatWord (int index) {
		switch (index) {
			case 0:
				return "";
			case 1:
				return "拾";
			case 2:
				return "佰";
			case 3:
				return "仟";
			case 4:
				return "萬";
			case 5:
				return "億";
			case 6:
				return "兆";
			default:
				return "";
		}
	}

	private String convertInteger (String number) {
		if (number.length () < 5) {
			if (number.length () == 1)
				return convertNumber (getDigitFromString (number, 0));
			String finalString = convertNumber (getDigitFromString (number, 0)) +
					getThatWord (number.length () - 1);
			number = number.substring (1);

			if (Integer.parseInt (number) == 0) {
				return finalString;
			}

			boolean addZero = false;
			for (int i = 0 ; i < number.length () ; i++) {
				if (number.charAt (i) == '0') {
					addZero = true;
					continue;
				}
				return finalString + (addZero? "零" : "") + convertInteger (number.substring (i));
			}
			return null;
		} else {
			int charsToRead = number.length () % 4;
			if (charsToRead == 0) charsToRead = 4;
			String firstPart = number.substring (0, charsToRead);
			String secondPart = number.substring (charsToRead);

			boolean addZero = false;
			for (int i = 0 ; i < secondPart.length () ; i++) {
				if (i == secondPart.length () - 1 && secondPart.charAt (i) == '0') {
					addZero = false;
					continue;
				}

				if (secondPart.charAt (i) == '0') {
					addZero = true;
					continue;
				}
				secondPart = secondPart.substring (i);
				break;
			}

			int thatWordIndex = 3 + number.length () / 4;

			if (charsToRead == 4) {
				thatWordIndex--;
			}
			String thatWord = getThatWord (thatWordIndex);

			return convertInteger (firstPart) + thatWord + (addZero? "零" : "") + (Integer.parseInt (secondPart) == 0 ? "" : convertInteger (secondPart));
		}
	}

	private int getDigitFromString (String str, int index) {
		return Integer.parseInt (Character.toString (str.charAt (index)));
	}
}
