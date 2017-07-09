package in.eloksolutions.ayyappa.controller;
import java.util.List;


public class Person {
	String id;
	String name;
	Address address;
	List<Integer> books;
	public Person(String id, String name) {
		this.id=id;
		this.name=name;
		
	}

	public List<Integer> getBookIds() {
		return books;
	}

	public void setBooks(List<Integer> books) {
		this.books = books;
	}

	class Address{
		String street;
		public Address(String street, String city, String phone, String zip,
				String state) {
			super();
			this.street = street;
			this.city = city;
			this.phone = phone;
			this.zip = zip;
			this.state = state;
		}
		String city;
		String phone;
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		String zip;
		String state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
}
