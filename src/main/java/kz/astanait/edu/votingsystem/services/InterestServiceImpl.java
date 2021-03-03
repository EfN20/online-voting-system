package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.InterestNotFoundException;
import kz.astanait.edu.votingsystem.models.Interest;
import kz.astanait.edu.votingsystem.repositories.InterestRepository;
import kz.astanait.edu.votingsystem.services.interfaces.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;

    @Autowired
    public InterestServiceImpl(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @Override
    public Interest findById(Long id) throws InterestNotFoundException {
        return interestRepository.findById(id).orElseThrow(InterestNotFoundException::new);
    }

    @Override
    public Interest save(Interest entity) {
        return interestRepository.save(entity);
    }

    @Override
    public void delete(Interest entity) {
        interestRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        interestRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return interestRepository.count();
    }

    @Override
    public List<Interest> findAll() {
        return interestRepository.findAll();
    }
}
