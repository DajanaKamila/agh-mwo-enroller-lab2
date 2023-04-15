package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		return connector.getSession().createCriteria(Participant.class).list();
	}
	public Collection<Participant> getAll(String sortBy, String sortOrder, String key) {
		String hql = "from Participant where login like :key";
//		String hql2 = "from Participant";
		if (sortBy.equals("login")){
			hql += " order by login";
			if (sortOrder.equals("ASC") || sortOrder.equals("DESC")){
				hql += " " + sortOrder;
			}
		}
		System.out.println(hql);

		Query query = connector.getSession().createQuery(hql);
		query.setParameter("key", "%"+key+"%");
		return query.list();
	}

//	public Collection<Participant> getAll(String key) {
//		String hql = "from Participant where login like :key";
//		Query query = connector.getSession().createQuery(hql);
//		query.setParameter("key", "%"+key+"%");
//		return query.list();
//	}

	public Participant findByLogin(String login) {
		return connector.getSession().get(Participant.class, login);
	}

	public Participant add(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
		return participant;
	}

	public void update(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(participant);
		transaction.commit();
	}

	public void delete(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
	}


}
