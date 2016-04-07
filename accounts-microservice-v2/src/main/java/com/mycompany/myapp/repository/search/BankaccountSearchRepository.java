package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Bankaccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bankaccount entity.
 */
public interface BankaccountSearchRepository extends ElasticsearchRepository<Bankaccount, Long> {
}
