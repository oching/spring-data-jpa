/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jpa.repository.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupportTests.UserRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for the setup of beans extending {@link QueryDslRepositorySupport}.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:querydsl.xml")
public class QueryDslRepositorySupportIntegrationTests {

	@Autowired
	UserRepository repository;

	@Autowired
	ReconfiguringUserRepositoryImpl reconfiguredRepo;

	@PersistenceContext(unitName = "querydsl")
	EntityManager em;

	@Test
	public void createsRepoCorrectly() {
		assertThat(repository, is(notNullValue()));
	}

	/**
	 * @see DATAJPA-135
	 */
	@Test
	public void createsReconfiguredRepoAccordingly() {

		assertThat(reconfiguredRepo, is(notNullValue()));
		assertThat(reconfiguredRepo.getEntityManager().getEntityManagerFactory(), is(em.getEntityManagerFactory()));
	}

	static class ReconfiguringUserRepositoryImpl extends QueryDslRepositorySupport {

		public ReconfiguringUserRepositoryImpl() {
			super(User.class);
		}

		@Override
		@PersistenceContext(unitName = "querydsl")
		public void setEntityManager(EntityManager entityManager) {
			super.setEntityManager(entityManager);
		}
	}

	static class EntityManagerContainer {

		@PersistenceContext(unitName = "querydsl")
		EntityManager em;
	}
}
