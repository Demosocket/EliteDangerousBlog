package com.demosocket.blog.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import javax.persistence.metamodel.SingularAttribute;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tag.class)
public abstract class Tag_ {

	public static volatile SingularAttribute<Tag, String> name;
	public static volatile SingularAttribute<Tag, Integer> id;
	public static volatile SetAttribute<Tag, Article> articles;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String ARTICLES = "articles";

}
