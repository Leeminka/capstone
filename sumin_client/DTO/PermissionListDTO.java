package DTO;

public class PermissionListDTO {
	private String information;
	private String name;
	private String permission;

	public PermissionListDTO() {
	}

	public PermissionListDTO(String paramString1, String paramString2,
			String paramString3) {
		this.permission = paramString1;
		this.name = paramString2;
		this.information = paramString3;
	}

	public String getInformation() {
		return this.information;
	}

	public String getName() {
		return this.name;
	}

	public String getPermission() {
		return this.permission;
	}

	public void setInformation(String paramString) {
		this.information = paramString;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setPermission(String paramString) {
		this.permission = paramString;
	}

	public String toString() {
		return "PermissionListDTO [permission = " + this.permission
				+ ", name = " + this.name + ", information = "
				+ this.information + "]";
	}
}
