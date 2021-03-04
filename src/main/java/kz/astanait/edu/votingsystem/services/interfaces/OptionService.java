package kz.astanait.edu.votingsystem.services.interfaces;

import kz.astanait.edu.votingsystem.models.Option;

public interface OptionService extends GenericService<Option> {
    void increaseOptionCount(Option option);
}
