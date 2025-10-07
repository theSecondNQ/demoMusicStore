package com.music_store.Service;

import com.music_store.Entity.Category;
import com.music_store.Dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public List<Category> getAllCategories() {
        return categoryDao.selectAll();
    }

    public Category getById(Integer id) {
        return categoryDao.selectById(id);
    }

    public boolean addCategory(Category category) {
        return categoryDao.insert(category) > 0;
    }

    public boolean updateCategory(Category category) {
        return categoryDao.update(category) > 0;
    }

    public boolean deleteCategory(Integer id) {
        return categoryDao.delete(id) > 0;
    }
}