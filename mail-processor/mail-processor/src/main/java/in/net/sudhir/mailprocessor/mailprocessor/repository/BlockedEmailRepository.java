package in.net.sudhir.mailprocessor.mailprocessor.repository;

import in.net.sudhir.mailprocessor.mailprocessor.model.BlockedEmailIds;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.repository
 User Name: SUDHIR
 Created Date: 20-08-2022 at 06:36
 Description:
 */

public interface BlockedEmailRepository extends CrudRepository<BlockedEmailIds, Integer> {
    List<BlockedEmailIds> findAll();
}
