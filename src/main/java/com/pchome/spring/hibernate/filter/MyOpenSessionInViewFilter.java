//package com.pchome.spring.hibernate.filter;
//
//import org.hibernate.FlushMode;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.dao.DataAccessResourceFailureException;
//import org.springframework.orm.hibernate4.SessionFactoryUtils;
//
//
//public class MyOpenSessionInViewFilter extends org.springframework.orm.hibernate4.support.OpenSessionInViewFilter {
//    /**
//     * we do a different flushmode than in the codebase here
//     */
//    protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
//        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
//        session.setFlushMode(FlushMode.AUTO);
//        return session;
//    }
//    
//    /**
//     * we do an explicit flush here just in case
//     * we do not have an automated flush
//     */
//    protected void closeSession(Session session, SessionFactory factory) {
//        session.flush();
//        super.closeSession(session, factory);
//    }
//}