package controller;

import Enums.SearchType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {

  //  Optional<T> save(final T t);

    Object save(final T t);

    boolean update(final T t);

    Optional<T> get(final String code);

    List<T> getAll();

    boolean delete(final Object o);

    ArrayList<T> search(final SearchType searchType, final String searchValue);
}