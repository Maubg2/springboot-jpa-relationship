package com.edison.springboot.jpa.springbootjparelationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.edison.springboot.jpa.springbootjparelationship.entities.Address;
import com.edison.springboot.jpa.springbootjparelationship.entities.Client;
import com.edison.springboot.jpa.springbootjparelationship.entities.ClientDetails;
import com.edison.springboot.jpa.springbootjparelationship.entities.Course;
import com.edison.springboot.jpa.springbootjparelationship.entities.Invoice;
import com.edison.springboot.jpa.springbootjparelationship.entities.Student;
import com.edison.springboot.jpa.springbootjparelationship.repositories.ClientDetailsRepository;
import com.edison.springboot.jpa.springbootjparelationship.repositories.ClientRepository;
import com.edison.springboot.jpa.springbootjparelationship.repositories.CourseRepository;
import com.edison.springboot.jpa.springbootjparelationship.repositories.InvoiceRepository;
import com.edison.springboot.jpa.springbootjparelationship.repositories.StudentRepository;

@SpringBootApplication
public class SpringbootJpaRelationshipApplication implements CommandLineRunner {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private ClientDetailsRepository clientDetailsRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaRelationshipApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		manyToManyBidireccionalRemove();
	}

	@Transactional
	public void manyToManyBidireccionalRemove(){
		Student student1 = new Student("Jano", "Pura");
		Student student2 = new Student("Erba", "Doe");
	
		Course course1 = new Course("Java Master", "Andres");
		Course course2 = new Course("Spring boot", "Andres");
	
		student1.setCourses(Set.of(course1, course2));
		student2.setCourses(Set.of(course2));
	
		course1.getStudents().add(student1);
		course2.getStudents().add(student1);
		course2.getStudents().add(student2);
	
		studentRepository.saveAll(Set.of(student1, student2));

		Optional<Student> studentOptional = studentRepository.findById(1L);
		if(studentOptional.isPresent()){
			Student studentDB = studentOptional.get();
			Optional<Course> courseOptional = courseRepository.findById(2L);
			if(courseOptional.isPresent()){
				Course courseDB = courseOptional.get();

				studentDB.getCourses().remove(courseDB);
				courseDB.getStudents().remove(studentDB);
	
				studentRepository.save(studentDB);
				System.out.println(studentDB);
			}
		}

	}	

	@Transactional
	public void manyToManyBidireccional(){
		Student student1 = new Student("Jano", "Pura");
		Student student2 = new Student("Erba", "Doe");
		Course course1 = new Course("Java Master", "Andres");
		Course course2 = new Course("Spring boot", "Andres");

		//student1.addCourse(course1);
		//student1.addCourse(course2);
		//student2.addCourse(course2);
		student1.setCourses(Set.of(course1, course2));
		student2.setCourses(Set.of(course2));
		course1.getStudents().add(student1);
		course2.getStudents().add(student1);
		course2.getStudents().add(student2);

		studentRepository.saveAll(Set.of(student1, student2));
	}
	/*public void addCourse(Course course){
        this.courses.add(course);
        course.getStudents().add(this);
    }*/

	@Transactional
	public void manyToManyRemoveFind(){
		Student student1 = new Student("Jano", "Pura");
		Student student2 = new Student("Erba", "Doe");

		Course course1 = new Course("Java Master", "Andres");
		Course course2 = new Course("Spring boot", "Andres");

		student1.setCourses(Set.of(course1, course2));
		student2.setCourses(Set.of(course2));

		studentRepository.saveAll(Set.of(student1, student2));

		System.out.println(student1);
		System.out.println(student2);

		Optional<Student> studentOptional = studentRepository.findById(1L);
		if(studentOptional.isPresent()){
			Student studentDB = studentOptional.get();
			Optional<Course> courseOptional = courseRepository.findById(2L);
			if(courseOptional.isPresent()){
				Course courseDB = courseOptional.get();
				studentDB.getCourses().remove(courseDB);
				studentRepository.save(studentDB);
				System.out.println(studentDB);
			}
		}
	}

	@Transactional
	public void manyToMany(){
		Student student1 = new Student("Jano", "Pura");
		Student student2 = new Student("Erba", "Doe");

		Course course1 = new Course("Java Master", "Andres");
		Course course2 = new Course("Spring boot", "Andres");

		student1.setCourses(Set.of(course1, course2));
		student2.setCourses(Set.of(course2));

		studentRepository.saveAll(Set.of(student1, student2));
	}

	@Transactional
	public void oneToOneBidireccional(){
		Client client = new Client("Eva", "Pura");
		ClientDetails clientDetails = new ClientDetails(true, 5000);
		
		client.setClientDetails(clientDetails);
		clientDetails.setClient(client);
		clientRepository.save(client);
	}

	@Transactional
	public void oneToOneFindById(){
		ClientDetails clientDetails = new ClientDetails(true, 5000);
		clientDetailsRepository.save(clientDetails);

		Optional<Client> clientOptional = clientRepository.findById(2L);
		clientOptional.ifPresent(client -> {
			client.setClientDetails(clientDetails);
			clientRepository.save(client);
	
			System.out.println(client);
		});
		
	}

	@Transactional
	public void oneToOne(){
		ClientDetails clientDetails = new ClientDetails(true, 5000);
		clientDetailsRepository.save(clientDetails);

		Client client = new Client("Eva", "Pura");
		client.setClientDetails(clientDetails);
		clientRepository.save(client);

		System.out.println(client);
	}

	@Transactional
	public void removeInvoiceBidireccionalFindById(){
		Optional<Client> optionalClient = clientRepository.findOneWithInvoices(1L);

		optionalClient.ifPresent(client -> {

			Invoice invoice1 = new Invoice("Compras de casa", 5000L);
			Invoice invoice2 = new Invoice("Compras de oficina", 8000L);
		
			client.setInvoices(Arrays.asList(invoice1, invoice2));

			invoice1.setClient(client);
			invoice2.setClient(client);
		
			clientRepository.save(client);
			System.out.println(client);
		});
		Optional<Client> optionalClientBd = clientRepository.findOneWithInvoices(1L);
		optionalClientBd.ifPresent(client -> {
			Optional<Invoice> invoiceOptional = invoiceRepository.findById(2L);
			invoiceOptional.ifPresent(invoice -> {
				client.getInvoices().remove(invoice);
				invoice.setClient(null);
				clientRepository.save(client);
				System.out.println(client);
			});
		});
	}

	@Transactional
	public void oneToManyInvoiceBidireccionalFindById(){
		Optional<Client> optionalClient = clientRepository.findOneWithInvoices(1L);

		optionalClient.ifPresent(client -> {

			Invoice invoice1 = new Invoice("Compras de casa", 5000L);
			Invoice invoice2 = new Invoice("Compras de oficina", 8000L);
		
			List<Invoice> invoices = new ArrayList<>();
			invoices.add(invoice1);
			invoices.add(invoice2);
			client.setInvoices(invoices);

			invoice1.setClient(client);
			invoice2.setClient(client);
		
			clientRepository.save(client);

			System.out.println(client);

		});
		
	}

	@Transactional
	public void oneToManyInvoiceBidireccional(){
		Client client = new Client("Frank", "Moras");

		Invoice invoice1 = new Invoice("Compras de casa", 5000L);
		Invoice invoice2 = new Invoice("Compras de oficina", 8000L);
		
		List<Invoice> invoices = new ArrayList<>();
		invoices.add(invoice1);
		invoices.add(invoice2);
		client.setInvoices(invoices);

		invoice1.setClient(client);
		invoice2.setClient(client);
		
		clientRepository.save(client);

		System.out.println(client);
	}

	@Transactional
	public void removeAddressesFindById(){
		Optional<Client> optionalClient = clientRepository.findById(2L);
		optionalClient.ifPresent(client -> {
			Address address1 = new Address("Palermo", 45);
			Address address2 = new Address("Marly", 51);
			
			client.setAddresses(Arrays.asList(address1, address2));

			clientRepository.save(client);
			System.out.println(client);

			Optional<Client> optionalClient2 = clientRepository.findOne(2L);
			optionalClient2.ifPresent(c -> {
				c.getAddresses().remove(address2);
				clientRepository.save(c);
				System.out.println(c);
			});
		});
	}

	@Transactional
	public void removeAddress(){
		Client client = new Client("Frank", "Moras");

		Address address1 = new Address("Palermo", 45);
		Address address2 = new Address("Marly", 51);
		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		clientRepository.save(client);

		System.out.println(client);

		Optional<Client> optionalClient = clientRepository.findById(3L);
		optionalClient.ifPresent(c -> {
			c.getAddresses().remove(address1);
			clientRepository.save(c);
			System.out.println(c);
		});
	}

	@Transactional
	public void oneToManyFindById(){
		Optional<Client> optionalClient = clientRepository.findById(2L);
		optionalClient.ifPresent(client -> {
			Address address1 = new Address("Palermo", 45);
			Address address2 = new Address("Marly", 51);
			
			client.setAddresses(Arrays.asList(address1, address2));

			clientRepository.save(client);
			System.out.println(client);
		});
	}

	@Transactional
	public void oneToMany(){
		Client client = new Client("Frank", "Moras");

		Address address1 = new Address("Palermo", 45);
		Address address2 = new Address("Marly", 51);
		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		clientRepository.save(client);

		System.out.println(client);
	}

	@Transactional
	public void manyToOne(){
		Client client = new Client("John", "Doe");
		clientRepository.save(client);

		Invoice invoice = new Invoice("Compras de oficina", 2000L);
		invoice.setClient(client);
		Invoice invoiceDB = invoiceRepository.save(invoice);
		System.out.println(invoiceDB);
	}

	@Transactional
	public void manyToOneFindById(){
		Optional<Client> optionalClient = clientRepository.findById(1L);
		if(optionalClient.isPresent()){

			Client client = optionalClient.orElseThrow();

			Invoice invoice = new Invoice("Compras de oficina", 2000L);
			invoice.setClient(client);
			Invoice invoiceDB = invoiceRepository.save(invoice);
			System.out.println(invoiceDB);

		}
	}
}
