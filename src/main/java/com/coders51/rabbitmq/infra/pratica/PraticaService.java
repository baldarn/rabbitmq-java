package com.coders51.rabbitmq.infra.pratica;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PraticaService {

    @Autowired
    IPraticaRepository praticaRepository;

    public List<Pratica> getAll() {
        return praticaRepository.findAll();
    }

    public Pratica getById(UUID id) {
        return praticaRepository.findById(id).orElse(null);
    }

    public Pratica create(Pratica p) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        p.setUpdatedAt(now);
        p.setCreatedAt(now);
        p.setId(UUID.randomUUID());
        return praticaRepository.save(p);
    }

    public Pratica update(Pratica p) {
        Pratica old = getById(p.getId());
        if (old == null) {
            return null;
        }
        old.setNome(p.getNome());
        old.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return praticaRepository.update(old);
    }

    public void delete(UUID id) {
        praticaRepository.deleteById(id);
    }

}
