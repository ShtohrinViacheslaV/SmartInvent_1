package com.smartinvent.repositories;


import com.smartinvent.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CompanyRepository interface
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * existsByName метод перевіряє чи існує компанія з вказаним ім'ям
     *
     * @param name ім'я компанії
     * @return true якщо компанія з вказаним ім'ям існує, інакше false
     */
    boolean existsByName(String name);

    /**
     * existsByPhone метод перевіряє чи існує компанія з вказаним телефоном
     *
     * @param phone телефон компанії
     * @return true якщо компанія з вказаним телефоном існує, інакше false
     */
    boolean existsByPhone(String phone);
}