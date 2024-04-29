package org.example.tagsandposts.dao.repository;

import org.example.tagsandposts.dao.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    List<TagEntity> findByIdIn(List<Long> id);
}
