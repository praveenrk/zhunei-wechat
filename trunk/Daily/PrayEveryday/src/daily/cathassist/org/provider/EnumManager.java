package daily.cathassist.org.provider;

public class EnumManager {
	public enum ContentType {
		LAUDES(0), HORAMEDIA(1), VESPERAE(2), COMPLETORIUM(3), MATUTINUM(4), MASS(
				5);
		private int value;

		private ContentType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static ContentType getContentType(int value) {
			ContentType type = null;
			switch (value) {
			case 0:
				type = ContentType.LAUDES;
				break;
			case 1:
				type = ContentType.HORAMEDIA;
				break;
			case 2:
				type = ContentType.VESPERAE;
				break;
			case 3:
				type = ContentType.COMPLETORIUM;
				break;
			case 4:
				type = ContentType.MATUTINUM;
				break;
			case 5:
				type= ContentType.MASS;
			}
			return type;

		}

		public static String getContentDataNameFromContentType(int value) {
			String string = "";
			switch (value) {
			case 0:
				string = "lod";
				break;
			case 1:
				string = "med";
				break;
			case 2:
				string = "ves";
				break;
			case 3:
				string = "comp";
				break;
			case 4:
				string = "let";
				break;
			case 5:
				string = "mass";
			}
			return string;
		}
	}
}
