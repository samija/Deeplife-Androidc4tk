package deeplife.gcme.com.deeplife.data_types;

public class Disciples {
	private String id,FirstName,MiddleName,Phone,Email,Country;
	
	public Disciples(String firstName, String middleName, String phone,
			String email, String country) {
		super();
		FirstName = firstName;
		MiddleName = middleName;
		Phone = phone;
		Email = email;
		Country = country;
	}
	public Disciples(){
		
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getMiddleName() {
		return MiddleName;
	}

	public void setMiddleName(String middleName) {
		MiddleName = middleName;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}
	
}
