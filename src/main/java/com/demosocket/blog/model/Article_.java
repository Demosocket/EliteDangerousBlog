package com.demosocket.blog.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import javax.persistence.metamodel.SingularAttribute;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Article.class)
public abstract class Article_ {

	public static volatile SingularAttribute<Article, Date> createdAt;
	public static volatile ListAttribute<Article, Comment> comments;
	public static volatile SingularAttribute<Article, Integer> id;
	public static volatile SingularAttribute<Article, String> text;
	public static volatile SingularAttribute<Article, String> title;
	public static volatile SingularAttribute<Article, User> user;
	public static volatile SingularAttribute<Article, Status> status;
	public static volatile SingularAttribute<Article, Date> updatedAt;
	public static volatile SetAttribute<Article, Tag> tags;

	public static final String CREATED_AT = "createdAt";
	public static final String COMMENTS = "comments";
	public static final String ID = "id";
	public static final String TEXT = "text";
	public static final String TITLE = "title";
	public static final String USER = "user";
	public static final String STATUS = "status";
	public static final String UPDATED_AT = "updatedAt";
	public static final String TAGS = "tags";

}
