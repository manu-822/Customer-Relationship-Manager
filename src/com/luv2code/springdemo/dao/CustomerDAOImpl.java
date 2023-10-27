package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.util.SortUtils;

@Repository
public class CustomerDAOImpl implements CustomerDAO 
{
	//need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
							//@Transactional     we remove this transactional and shift it to service layer 
							// this lets spring start and end the transaction, now we dont need to add session.getTransaction and commit
	public List<Customer> getCustomers(int theSortField) 
	{
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		//determine sort field
		String theFieldName=null;
		
		switch(theSortField)
		{
			case SortUtils.FIRST_NAME:
					theFieldName="firstName";
					break;
			
			case SortUtils.LAST_NAME:
				theFieldName="lastName";
				break;
				
			case SortUtils.EMAIL:
				theFieldName="Email";
				break;
			
			default:
				//if nothing matches the default to sort by lastName
				theFieldName="lastName";
		}
		
		//create a query
		String queryString="from Customer order by "+ theFieldName;
		Query<Customer> query=currentSession.createQuery(queryString,Customer.class);
		
		//execute query and get result list
		List<Customer> customers = query.getResultList();
		
		//return the result
		return customers;
	}


	@Override
	public void saveCustomer(Customer theCustomer) 
	{
		//get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		//save the customer
		currentSession.saveOrUpdate(theCustomer);  // saveOrUpdate checks if primary key/id empty then it insert else update
		
	}


	@Override
	public Customer getCustomer(int theId) 
	{
		Session currentSession = sessionFactory.getCurrentSession();
		Customer theCustomer = currentSession.get(Customer.class, theId);
		return theCustomer;
	}


	@Override
	public void deleteCustomer(int theId)
	{
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete the customer with the primary key (id that is being passed in)
		Query query= currentSession.createQuery("delete from Customer where id=:customerId");
		
		query.setParameter("customerId",theId);
		
		query.executeUpdate();
		
	}


	@Override
	public List<Customer> searchCustomers(String theSearchName) 
	{
		//get current session
		Session currentSession= sessionFactory.getCurrentSession();
		
		Query query=null;
		
		//
		// only search by name if search name is not empty
		//
		
		if(theSearchName!=null && theSearchName.trim().length()>0)
		{
			// search for first name or last name ... case insensitive
			query= currentSession.createQuery("from Customer where lower(firstName) like :theName "
					   													+ "or lower(lastName) like :theName",Customer.class);
			
			query.setParameter("theName","%" + theSearchName.toLowerCase()+"%");
		}
		else
		{
			//theSearchName is empty...so just get all customers
			query = currentSession.createQuery("from Customer",Customer.class);
		}
		
		//execute the query and get result list
		List<Customer> customers=query.getResultList();
		
		return customers;
	}


	


	
	
	

}
