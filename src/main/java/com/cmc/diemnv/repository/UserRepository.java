package com.cmc.diemnv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmc.diemnv.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public List<User> findByEmail(String email);

	@Query(value = "select * from personal p where concat(p.firstname, p.lastname, p.email) like %:sequence% limit :begin, :numberRecord", nativeQuery = true)
	public List<User> findBySequence(@Param("sequence") String sequence, @Param("begin") int begin,
			@Param("numberRecord") int numberRecord);

}
