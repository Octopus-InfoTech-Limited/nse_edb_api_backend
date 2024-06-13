package com.octopus_tech.share.db;

import java.io.Closeable;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 * A helper class to manipulating hibernate with write less do more principle<br />
 * try cache style should be used<br />
 * <br />
 * try(DBHelper dbHelper = new DBHelper())<br />
 * {<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;//your statements here<br />
 * }<br />
 * catch(Exception e)<br />
 * {<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;e.printStackTrace();<br />
 * }<br />
 * 
 * @author KSG Yeung &lt;pw.yeung@octopus-tech.com&gt;
 *
 */
public final class DBHelper implements Closeable
{
	/**
	 * Store instances to prevent open more then one connection at same thread
	 */
	private final static HashSet<Session> INSTANCE_GUARD = new HashSet<Session>();
	
	/**
	 * Store hibernate session 
	 */
	private Session session;
	/**
	 * Store current transaction
	 */
	private Transaction transaction;
	
	SessionFactory sessionFactory;
	
	Logger logger;
	
	/**
	 * Constructor of DBHelper, the instance must be close() after database operation is ended, 
	 * otherwise IllegalAccessError will be thrown at next DBHelper construction
	 */
	public DBHelper()
	{
		logger = LogManager.getLogger(DBHelper.class);
		
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.getCurrentSession();
		logger.debug("*** DBHelper.DBHelper() ***");
		
		if(INSTANCE_GUARD.contains(session))
		{
			throw new IllegalAccessError("DBHelper is in use, did u forgot close previous DBHelper?");
		}
		INSTANCE_GUARD.add(session);
	}
	
	/**
	 * Create or open the transaction, usually this function is not required call at anytime<br />
	 * Because this function will call automatically when if needed<br />
	 * <b>Note: Transaction is managed by DBHelper, So DO NOT call Transaction.commit or Transaction.rollback at any time</b><br />
	 * @return the transaction which ready for use
	 */
	public Transaction beginTransactionIfNeeded()
	{
		if(transaction == null)
		{
			transaction = session.beginTransaction();
		}
		return transaction;
	}
	
	/**
	 * Retrieve hibernate session.<br />
	 * <b>DONT</b> call Session.close on this session at any time.
	 * @return hibernate session, null value may return if session is not established
	 */
	public Session getHibernateSession()
	{
		return session;
	}
	
	/**
	 * Advance operation for hibernate
	 * @param func - callback will be called when is ready, null value is also accept(do nothing)
	 */
	public void customHibernateAction(HibernateCustomAction func)
	{
		Transaction transaction = beginTransactionIfNeeded();
		if(func != null)
		{
			func.onHibernateCustomAction(session, transaction);
		}
	}
	
	/**
	 * Query a single row
	 * @param <T> - type of module class
	 * @param clazz - module class
	 * @param id - the id of the row
	 * @return retrieved row
	 * @throws Error id field is not found
	 */
	public <T> Optional<T> queryOne(Class<T> clazz, long id)
	{
		if(clazz == null)
		{
			throw new IllegalArgumentException();
		}
		
		//java.lang.reflect.Type type = T.class.getClass();
		java.lang.reflect.Field targetField = null;
		
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		for(java.lang.reflect.Field field : fields)
		{
			if(field.isAnnotationPresent(IdAttribute.class))
			{
				targetField = field;
				break;
			}
			
			if("id".equals(field.getName()))
			{
				targetField = field;
				break;
			}
		}
		
		if(targetField == null)
		{
			throw new Error("no id field found!");
		}
		
		return queryOne(clazz, targetField.getName(), id);
	}
	
	/**
	 * Query a single row
	 * @param <T> - type of module class
	 * @param clazz - module class
	 * @param idName - id field name
	 * @param id - the row id
	 * @return retrieved row
	 */
	public <T> Optional<T> queryOne(Class<T> clazz, String idName, long id)
	{
		if(clazz == null || idName == null || "".equals(idName))
		{
			throw new IllegalArgumentException();
		}
		
		return queryOne(clazz, idName, Long.valueOf(id));
	}

	/**
	 * Query a single row
	 * @param <T> - type of module class
	 * @param <U> - type of field value
	 * @param clazz - module class
	 * @param fieldName - field name
	 * @param value - the field value, null value is accept
	 * @return retrieved row
	 */
	public <T, U> Optional<T> queryOne(Class<T> clazz, String fieldName, U value)
	{
		if(clazz == null || fieldName == null)
		{
			throw new IllegalArgumentException();
		}
		
		if(value == null)
		{
			return queryOne(clazz, (cb, root) -> cb.isNull(root.get(fieldName)));
		}
		
		return queryOne(clazz, (cb, root) -> cb.equal(root.get(fieldName), value));
	}

	/**
	 * Query a single row
	 * @param <T> - type of module class
	 * @param clazz - module class
	 * @param where - simple criteria callback
	 * @return
	 */
	public <T> Optional<T> queryOne(Class<T> clazz, WhereClause<T> where)
	{
		if(clazz == null)
		{
			throw new IllegalArgumentException();
		}
		
		return this.query(clazz, (cb, root, where2) ->
		{
			where2.where(where.where(cb, root)).limit(1);
		}).stream().findFirst();
	}
	
	/**
	 * Query all rows from a table
	 * @param <T> - type of module class
	 * @param clazz - module class
	 * @return all rows in table
	 */
	public <T> List<T> query(Class<T> clazz)
	{
		if(clazz == null)
		{
			throw new IllegalArgumentException();
		}
		
		beginTransactionIfNeeded();
		return query(clazz, (WhereClause2<T>)null);
	}
	
	/**
	 * Query rows from a table
	 * @param <T> - type of module class
	 * @param clazz - module class
	 * @param where simple condition control callback
	 * @return rows match all the conditions in table
	 */
	public <T> List<T> query(Class<T> clazz, WhereClause<T> where)
	{
		if(clazz == null || where == null)
		{
			throw new IllegalArgumentException();
		}
		
		return query(clazz, (cb, root, where2) -> where2.where(where.where(cb, root)));
	}

	/**
	 * Query rows from a table
	 * @param <T> - type of module class
	 * @param clazz - module class
	 * @param where - advance condition control callback
	 * @return rows match all the conditions in table
	 */
	public <T> List<T> query(Class<T> clazz, WhereClause2<T> where)
	{
		if(clazz == null)
		{
			throw new IllegalArgumentException();
		}
		
		beginTransactionIfNeeded();
		
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> root = cq.from(clazz);
		
		class Struct
		{
			public Predicate predicate = null;
			public ArrayList<Order> orders = new ArrayList<>();
			public ArrayList<Expression<?>> groups = new ArrayList<Expression<?>>();
			public Integer limit = null;
			public Integer offset = null;
			public boolean distinct = false;
		}
		final Struct struct = new Struct();
		
		WhereClause2Condition<T> obgb = new WhereClause2Condition<T>() 
		{
			@Override
			public WhereClause2Condition<T> sortDesc(Expression<T> path) 
			{
				struct.orders.add(cb.desc(path));
				return this;
			}
			
			@Override
			public WhereClause2Condition<T> sortAsc(Expression<T> path) 
			{
				struct.orders.add(cb.asc(path));
				return this;
			}
			
			@Override
			public WhereClause2Condition<T> groupBy(Expression<T> path)
			{
				struct.groups.add(path);
				return this;
			}

			@Override
			public WhereClause2Condition<T> limit(int count) 
			{
				struct.limit = count;
				return this;
			}

			@Override
			public WhereClause2Condition<T> distinct() 
			{
				struct.distinct = true;
				return this;
			}

			@Override
			public WhereClause2Condition<T> where(WhereClause2ConditionWhere<T> where)
			{
				if(where == null)
				{
					struct.predicate = null;
				}
				else
				{
					struct.predicate = where.where(struct.predicate);
				}
				return this;
			}

			@Override
			public WhereClause2Condition<T> where(Predicate p)
			{
				struct.predicate = p;
				return this;
			}

			@Override
			public WhereClause2Condition<T> offset(int offset) 
			{
				struct.offset = offset;
				return this;
			}
		};

		if(where != null)
		{
			where.where(cb, root, obgb);
			if(struct.predicate != null)
			{
				cq.where(struct.predicate);
			}
			if(struct.orders.size() > 0)
			{
				cq.orderBy(struct.orders);
			}
			if(struct.groups.size() > 0)
			{
				cq.groupBy(struct.groups);
			}
			cq.distinct(struct.distinct);
		}

		TypedQuery<T> tq = session.createQuery(cq);
		if(struct.offset != null && struct.offset >= 0)
		{
			tq.setFirstResult(struct.offset);
		}
		if(struct.limit != null && struct.limit >= 0)
		{
			tq.setMaxResults(struct.limit);
		}
		List<T> list = tq.getResultList();
		return list;
	}
	
	/**
	 * Update a row
	 * @param <T> - module class
	 * @param obj - a row
	 */
	public <T> void update(T obj)
	{
		beginTransactionIfNeeded();
		if(obj == null)
		{
			throw new IllegalArgumentException("obj is null");
		}

		session.update(obj);
	}

	/**
	 * Save a row
	 * @param <T> - module class
	 * @param obj - a row
	 */
	public <T> void save(T obj)
	{
		beginTransactionIfNeeded();
		if(obj == null)
		{
			throw new IllegalArgumentException("obj is null");
		}
		
		session.save(obj);
	}

	/**
	 * Update or save a row
	 * @param <T> - module class
	 * @param obj - a row
	 */
	public <T> void saveOrUpdate(T obj)
	{
		beginTransactionIfNeeded();
		if(obj == null)
		{
			throw new IllegalArgumentException("obj is null");
		}
		
		session.saveOrUpdate(obj);
	}

	/**
	 * delete a row
	 * @param <T> - module class
	 * @param obj - a row
	 */
	public <T> void delete(T obj)
	{
		beginTransactionIfNeeded();
		if(obj == null)
		{
			throw new IllegalArgumentException("obj is null");
		}

		session.delete(obj);
	}

	/**
	 * Update a row
	 * @param <T> - module class
	 * @param obj - a row, do nothing is null value provided
	 */
	public <T> void update(Optional<T> obj)
	{
		beginTransactionIfNeeded();
		obj.ifPresent(session::update);
	}

	/**
	 * Save a row
	 * @param <T> - module class
	 * @param obj - a row, do nothing is null value provided
	 */
	public <T> void save(Optional<T> obj)
	{
		beginTransactionIfNeeded();
		obj.ifPresent(session::save);
	}

	/**
	 * Update or save a row
	 * @param <T> - module class
	 * @param obj - a row, do nothing is null value provided
	 */
	public <T> void saveOrUpdate(Optional<T> obj)
	{
		beginTransactionIfNeeded();
		obj.ifPresent(session::saveOrUpdate);
	}

	/**
	 * Delete a row
	 * @param <T> - module class
	 * @param obj - a row, do nothing is null value provided
	 */
	public <T> void delete(Optional<T> obj)
	{
		beginTransactionIfNeeded();
		obj.ifPresent(session::delete);
	}
	
	/**
	 * Refresh an object from database
	 * @param <T> - module class
	 * @param obj - a row to be refreshed
	 */
	public <T> void refresh(T obj)
	{
		beginTransactionIfNeeded();
		session.refresh(obj);
	}

	/**
	 * Refresh an object from database
	 * @param <T> - module class
	 * @param obj - a row to be refreshed, do nothing if null value provided
	 */
	public <T> void refresh(Optional<T> obj)
	{
		beginTransactionIfNeeded();
		obj.ifPresent(session::refresh);
	}

	/**
	 * Refresh objects list from database
	 * @param <T> - module class
	 * @param obj - rows to be refreshed
	 */
	public <T> void refresh(List<? extends T> list)
	{
		beginTransactionIfNeeded();
		list.forEach(session::refresh);
	}

	/**
	 * Save multiple rows
	 * @param objects - list of rows, null values will result in thrown Exception
	 * @exception IllegalArgumentException
	 */
	public void save(Object ...objects)
	{
		System.out.println("*** DBHelper.save() ***");
		beginTransactionIfNeeded();
		

		if(objects == null)
		{
			throw new IllegalArgumentException("objects is null");
		}
		
		if(Arrays.asList(objects).contains(null))
		{
			throw new IllegalArgumentException("objects contains null values");
		}
		
		for(Object object:objects)
		{
			session.save(object);
		}
	}
	
	public void flush()
	{
		session.flush();
	}
	
	/**
	 * Commit all pending changes and operations to database, the function should manually call at the end of block at most of the time<br />
	 * If this function not called at end of the block, rollback will be called automatically
	 */
	public void commit()
	{
		beginTransactionIfNeeded();
		transaction.commit();
		transaction = null;
	}
	
	/**
	 * Discard all changes from pending operations, this function will be called automatically at end of block if commit was not called
	 */
	public void rollback()
	{
		beginTransactionIfNeeded();
		transaction.rollback();
		transaction = null;
	}

	/**
	 * Close the connection to database and process all pending operations<br />
	 * try catch should be use, and this function should not be call
	 */
	@Override
	public void close() throws IOException 
	{
		logger.debug("*** DBHelper.close() ***");
		if(session.isOpen() && transaction != null)
		{
			if(!session.isDefaultReadOnly())
			{
				try
				{
					if(transaction.getStatus() == TransactionStatus.ACTIVE)
					{
						transaction.rollback();
					}
					else if(transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)
					{
						transaction.rollback();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		//session.close();
		INSTANCE_GUARD.remove(session);
		
		transaction = null;
		session = null;
	}
	
	/**
	 * Simple condition
	 */
	@FunctionalInterface
	public interface WhereClause<T>
	{
		/**
		 * Construct the condition
		 * @param cb - short form of CriteriaBuilder
		 * @param root
		 * @return condition
		 * @see Predicate
		 * @see CriteriaBuilder
		 * @see Root
		 */
		Predicate where(CriteriaBuilder cb, Root<T> root);
	}

	/** 
	 * Where condition with fluent interface style
	 */
	@FunctionalInterface
	public interface WhereClause2ConditionWhere<T>
	{
		/**
		 * Construct the condition
		 * @param lastPredicate - last condition, null if no condition was constructed
		 * @return condition
		 * @see Predicate
		 */
		Predicate where(Predicate lastPredicate);
	}
	
	public interface WhereClause2Condition<T>
	{
		/**
		 * WHERE statement on SQL
		 * @param p - Where condition with fluent interface style
		 * @see WhereClause2ConditionWhere
		 */
		WhereClause2Condition<T> where(WhereClause2ConditionWhere<T> where);
		/**
		 * WHERE statement on SQL
		 * @param p - condition
		 */
		WhereClause2Condition<T> where(Predicate p);
		/**
		 * Sort rows with field by ascending order<br />
		 * <b>Using java to sort rows are recommended</b>
		 * @param path - field
		 */
		WhereClause2Condition<T> sortAsc(Expression<T> path);
		/**
		 * Sort rows with field by descending order<br />
		 * <b>Using java to sort rows are recommended</b>
		 * @param path - field
		 */
		WhereClause2Condition<T> sortDesc(Expression<T> path);
		/**
		 * GROUP BY statement on SQL
		 * @param path - field to be group
		 */
		WhereClause2Condition<T> groupBy(Expression<T> path);
		/**
		 * Skip first rows from output
		 * @param offset - count of first rows will be skipped from output, &lt;=0 is not skipping rows from output
		 */
		WhereClause2Condition<T> offset(int offset);
		/**
		 * Limit the rows count of output
		 * @param count - number of rows u wanted, &lt;=0 is want all rows
		 */
		WhereClause2Condition<T> limit(int count);
		/**
		 * Enable keyword "DISTINCT" on SQL
		 */
		WhereClause2Condition<T> distinct();
	}

	/**
	 * Advance condition control callback
	 */
	@FunctionalInterface
	public interface WhereClause2<T>
	{
		void where(CriteriaBuilder cb, Root<T> root, WhereClause2Condition<T> wc2c);
	}

	/**
	 * Advance control for hibernate callback
	 */
	@FunctionalInterface
	public interface HibernateCustomAction
	{
		void onHibernateCustomAction(Session session, Transaction transaction);
	}
	
	/**
	 * Define a field as id field
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface IdAttribute
	{
	}
}