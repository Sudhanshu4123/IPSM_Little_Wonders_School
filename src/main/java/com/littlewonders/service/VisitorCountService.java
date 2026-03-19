package com.littlewonders.service;

import com.littlewonders.model.VisitorCount;
import com.littlewonders.repository.VisitorCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Service
public class VisitorCountService {

    @Autowired
    private VisitorCountRepository repository;

    private static final Long START_COUNT = 100000L;

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            repository.save(new VisitorCount(START_COUNT));
        }
    }

    @Transactional
    public Long incrementAndGet() {
        VisitorCount visitorCount = repository.findAll().stream().findFirst()
                .orElseGet(() -> new VisitorCount(START_COUNT));
        visitorCount.setCount(visitorCount.getCount() + 1);
        return repository.save(visitorCount).getCount();
    }

    public Long getCount() {
        return repository.findAll().stream().findFirst()
                .map(VisitorCount::getCount)
                .orElse(START_COUNT);
    }
}
