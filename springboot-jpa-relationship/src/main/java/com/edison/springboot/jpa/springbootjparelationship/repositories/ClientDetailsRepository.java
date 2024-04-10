package com.edison.springboot.jpa.springbootjparelationship.repositories;

import org.springframework.data.repository.CrudRepository;

import com.edison.springboot.jpa.springbootjparelationship.entities.ClientDetails;

public interface ClientDetailsRepository extends CrudRepository<ClientDetails, Long>{

}
