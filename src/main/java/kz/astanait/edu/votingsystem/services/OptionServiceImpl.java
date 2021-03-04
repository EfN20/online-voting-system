package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.OptionNotFoundException;
import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.repositories.OptionRepository;
import kz.astanait.edu.votingsystem.services.interfaces.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public Option findById(Long id) {
        return optionRepository.findById(id).orElseThrow(OptionNotFoundException::new);
    }

    @Override
    public Option save(Option entity) {
        return optionRepository.save(entity);
    }

    @Override
    public void delete(Option entity) {
        optionRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        optionRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return optionRepository.count();
    }

    @Override
    public List<Option> findAll() {
        return optionRepository.findAll();
    }

    @Transactional
    @Override
    public void increaseOptionCount(Option option) {
        option.setVoteCount(option.getVoteCount() + 1);
    }
}
