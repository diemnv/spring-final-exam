package com.cmc.diemnv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cmc.diemnv.common.UserConstant;
import com.cmc.diemnv.common.Validator;
import com.cmc.diemnv.entity.User;
import com.cmc.diemnv.repository.UserRepository;

@Service
public class UserServiceImpl {
	@Autowired
	private UserRepository userRepository;

	public boolean insertUser(User user, Model model) {
		boolean success = true;
		if (user.getFirstName().length() <= 0) {
			model.addAttribute("firstNameError", UserConstant.FIRST_NAME_ERROR);
			success = false;
		}
		if (user.getLastName().length() <= 0) {
			model.addAttribute("lastNameError", UserConstant.LAST_NAME_ERROR);
			success = false;
		}
		if (!Validator.validateEmail(user.getEmail()) || userRepository.findByEmail(user.getEmail()).size() > 0) {
			model.addAttribute("emailError", UserConstant.EMAIL_ERROR);
			success = false;
		}

		if (user.getMobile().length() < 9 || user.getMobile().length() > 10) {
			model.addAttribute("mobileError", UserConstant.PHONE_ERROR);
			success = false;
		}
		if (success) {
			userRepository.save(user);
		}
		return success;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	public User getUserById(Long id) {
		return userRepository.getOne(id);
	}

	public boolean updateUser(User user) {
		User newUser = userRepository.save(user);
		return newUser != null;
	}

	public List<User> getUserListBySequence(String sequence, int begin, int recordNumber) {
		return userRepository.findBySequence(sequence, 0, Integer.MAX_VALUE);
	}
}
