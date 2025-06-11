package com.smartinvent.repositories;

import com.smartinvent.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/* * Репозиторій для роботи з товарами в базі даних.
 * Використовується для виконання CRUD операцій над сутністю Product.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /* * Знаходить усі товари, назва яких містить вказаний текст (без урахування регістру).
     * @param name частина назви товару для пошуку
     * @return список товарів, що відповідають критерію пошуку
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /* * Знаходить товар за його унікальним унікальним кодом товару.
     * @param productWorkId унікальний робочий ID товару
     * @return Optional з товаром, якщо знайдено, або порожній Optional, якщо не знайдено
     */
    Optional<Product> findByProductWorkId(String productWorkId);

    /* * Перевіряє, чи існує товар з вказаним унікальним кодом товару.
     * @param productWorkId унікальний робочий ID товару
     * @return true, якщо товар існує, інакше false
     */
    boolean existsByProductWorkId(String productWorkId);
}


