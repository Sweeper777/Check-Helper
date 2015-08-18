package com.checkhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;

public class MainActivity extends Activity {
	public EditText textField;
	public Button convertBtn;
	public TextView text;
	public Button chineseBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_main);

		textField = (EditText)findViewById (R.id.text_field);
		convertBtn = (Button)findViewById (R.id.button);
		chineseBtn = (Button)findViewById (R.id.button_chinese);
		text = (TextView)findViewById (R.id.textView);

		convertBtn.setOnClickListener (new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				text.setText (convertNumberString (textField.getText ().toString ()));
			}
		});

		chineseBtn.setOnClickListener (new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				startActivity (new Intent (MainActivity.this, MainActivityChinese.class));
			}
		});
	}

	private String convertNumberString (String number) {
		if (number.equals ("")) {
			return "Please Enter Amount";
		}

		String[] twoParts = number.split ("\\.");
		String integerPart = twoParts[0];
		String decimalPart;
		try {
			decimalPart = twoParts[1];
		} catch (ArrayIndexOutOfBoundsException ex) {
			decimalPart = "";
		}
		String integerString;
		String decimalString;

		if (integerPart.length () < 4 && integerPart.length () > 0) {
			integerString = get1To3DigitString (integerPart);
		} else if (integerPart.equals ("")) {
			integerString = "";
		} else {
			if (new BigInteger (integerPart).compareTo (new BigInteger ("999999999999999999")) > 0) {
				return "The number is too big. Please enter a number less than 999999999999999999";
			}

			if (new BigInteger (integerPart).compareTo (new BigInteger ("1")) < 0) {
				return "The number is too small, Please enter a number larger than 1";
			}

			int commaTimes = integerPart.length () / 3;

			if (integerPart.length () % 3 == 0) {
				commaTimes--;
			}

			String[] groups = new String[commaTimes + 1];
			integerPart = new StringBuilder (integerPart).reverse ().toString ();
			for (int i = 0 ; i < groups.length ; i++) {
				try {
					groups[i] = integerPart.substring (i * 3, i * 3 + 3);
				} catch (IndexOutOfBoundsException ex) {
					groups[i] = integerPart.substring (i * 3);
				}
				groups[i] = new StringBuilder (groups[i]).reverse ().toString ();
				groups[i] = get1To3DigitString (groups[i]) + " " + getTheWord (i) + " ";
			}

			reverseArray (groups);
			StringBuilder builder = new StringBuilder ("");
			for (int i = 0 ; i < groups.length ; i++) {
				builder.append (groups[i]);
			}
			integerString = builder.toString ();
		}

		if (decimalPart.equals ("") || Integer.parseInt (decimalPart) == 0) {
			decimalString = "and no cents only";
		} else {
			try {
				decimalPart = decimalPart.substring (0, 2);
			} catch (IndexOutOfBoundsException e) {
				decimalPart += "0";
			}
			if (Integer.parseInt (decimalPart) == 1) {
				decimalString = "and " + get2DigitString (Integer.parseInt (decimalPart)) +
						" cent only";
			} else {
				decimalString = "and " + get2DigitString (Integer.parseInt (decimalPart)) +
						" cents only";
			}
		}

		if (integerString.equals ("one")) {
			return toProper (integerString + " dollar " + decimalString);
		} else {
			return toProper (integerString + " dollars " + decimalString);
		}
	}

	private void reverseArray(String[] array) {
		for(int i = 0; i < array.length / 2; i++)
		{
			String temp = array[i];
			array[i] = array[array.length - i - 1];
			array[array.length - i - 1] = temp;
		}
	}

	public String get1To3DigitString (String number) {
		Integer i = Integer.parseInt (number);
		number = i.toString ();

		if (number.length () == 3) {
			return get3DigitString (number);
		} else if (number.length () == 2) {
			return get2DigitString (Integer.parseInt (number));
		} else if (number.length () == 1) {
			return get1DigitString (Integer.parseInt (number));
		} else {
			return "";
		}
	}

	@NonNull
	private String get3DigitString(String number) {
		return get1DigitString (getDigitFromString (number, 0)) +
			" hundred " + get2DigitString (Integer.parseInt (number.substring (1)));
	}

	private String get1DigitString(int i) {
		switch (i) {
			case 1:
				return "one";
			case 2:
				return "two";
			case 3:
				return "three";
			case 4:
				return "four";
			case 5:
				return "five";
			case 6:
				return "six";
			case 7:
				return "seven";
			case 8:
				return "eight";
			case 9:
				return "nine";
			case 10:
				return "ten";
			case 80:
				return "eighty";
			case 60:
			case 70:
			case 90:
				return get1DigitString (i / 10) + "ty";
			case 20:
				return "twenty";
			case 30:
				return "thirty";
			case 40:
				return "forty";
			case 50:
				return "fifty";
			default:
				return "";
		}
	}

	private String get2DigitString(int i) {
		if (i % 10 == 0 || Integer.toString (i).length () == 1) {
			return get1DigitString (i);
		}

		if (Integer.toString (i).length () != 2)
			throw new AssertionError ();

		String s = Integer.toString (i);
		int tensDigit = Integer.parseInt (Character.toString (s.charAt (0)));
		int onesDigit = Integer.parseInt (Character.toString (s.charAt (1)));

		if (tensDigit != 1) {
			return get1DigitString (tensDigit * 10) + "-" + get1DigitString (onesDigit);
		}

		switch (i) {
			case 11:
				return "eleven";
			case 12:
				return "twelve";
			case 13:
				return "thirteen";
			case 14:
				return "fourteen";
			case 15:
				return "fifteen";
			case 18:
				return "eighteen";
			case 19:
			case 16:
			case 17:
				return get1DigitString (onesDigit) + "teen";
			default:
				return "";
		}
	}

	private int getDigitFromString (String str, int index) {
		return Integer.parseInt (Character.toString (str.charAt (index)));
	}

	private String getTheWord(int i) {
		switch (i) {
			case 0: return "";
			case 1: return "thousand";
			case 2: return "milion";
			case 3: return "billion";
			case 4: return "trillion";
			case 5: return "quadrillion";
			default: return "";
		}
	}

	private String toProper (String str) {
		String[] words = str.split (" ");
		StringBuilder builder;
		for (int i  = 0 ; i < words.length ; i++) {
			if (words[i].equals ("") || words[i].equals ("and"))
				continue;

			builder = new StringBuilder (words[i]);
			builder.setCharAt (0, ((Character)words[i].charAt (0)).toString ().toUpperCase ().charAt (0));
			words[i] = builder.toString ();
		}

		builder = new StringBuilder ("");

		for (int i  = 0 ; i < words.length ; i++) {
			if (words[i].equals (""))
				continue;
			builder.append (words[i]);
			builder.append (" ");
		}

		return builder.toString ();
	}
}
