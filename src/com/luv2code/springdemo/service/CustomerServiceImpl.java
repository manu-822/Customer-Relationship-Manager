package com.luv2code.springdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.dao.CustomerDAO;
import com.luv2code.springdemo.entity.Customer;

@Service
public class CustomerServiceImpl implements CustomerService 
{
	
	//nned to inject customer dao
	@Autowired
	private CustomerDAO customerDAO;

	
	// this lets spring start and end the transaction, now we dont need to add session.getTransaction and commit
	// hum CustomerDAOImpl se transactional hta kr yahan use krenge. basically this layer will act as an middle layer 
	//and it will handle all the different dao related tasks
	@Override
	@Transactional    
	public List<Customer> getCustomers(int theSortField) 
	{
		return customerDAO.getCustomers(theSortField);
	}
	
	@Transactional
	public void saveCustomer(Customer theCustomer)
	{
		customerDAO.saveCustomer(theCustomer);
	}

	@Override
	@Transactional
	public Customer getCustomer(int theId) 
	{
		return customerDAO.getCustomer(theId);
	}

	@Override
	@Transactional
	public void deleteCustomer(int theId)
	{
		customerDAO.deleteCustomer(theId);
	}

	@Override
	@Transactional
	public List<Customer> searchCustomer(String theSearchName) 
	{
		List<Customer> theCustomers= customerDAO.searchCustomers(theSearchName);
		return theCustomers;
	}

}
