package org.ergea.foodapp.repository;

import org.ergea.foodapp.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    List<Merchant> findAllByIsOpen(Boolean isOpen);

    @Transactional
    @Procedure("delete_merchant")
    void deleteQuerySP(@PathVariable("id") UUID id);

    @Transactional
    @Procedure("update_merchant")
    void updateQuerySP(@Param("p_id") UUID id,
                       @Param("p_merchant_name") String name,
                       @Param("p_merchant_location") String location,
                       @Param("p_open") Boolean open);

    @Procedure("insert_merchant")
    void createQuerySP(@Param("p_id") UUID id,
                       @Param("p_merchant_name") String name,
                       @Param("p_merchant_location") String location,
                       @Param("p_open") Boolean open
    );

    @Query(value = "SELECT * FROM get_merchant_by_id(:id)", nativeQuery = true)
    Object findByIdQuerySP(@Param("id") UUID id);

}
