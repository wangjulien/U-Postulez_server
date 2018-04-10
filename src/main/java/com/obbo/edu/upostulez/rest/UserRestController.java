package com.obbo.edu.upostulez.rest;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.exception.DaoException;
import com.obbo.edu.upostulez.service.IUserService;

/**
 * Classe qui regroupe tous les traitements concernant un Conseiller courrant. -
 * Ajouter un User - Recuperer un User par son ID, lire toutes ces informations
 * (data) - Modifier un User - Supprimer un User - Lister tous les User dans
 * persistence - ToDo : simulationCredit et gestionPatrimoine
 * 
 * DaoUser est utilise ici pour Chercher ou Modifier l'information dans
 * persistance *
 * 
 * @author JW
 *
 */

@RestController
@RequestMapping("/u-postulez")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	private IUserService userService;

	public UserRestController() {
		super();
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> findUserById(@PathVariable(value = "id") Long userId) throws DaoException {
		LOGGER.info("Find a user by the id : " + userId);

		User foundUser = userService.findById(userId)
				.orElseThrow(() -> new DaoException("User with id : " + userId + " is not found"));

		LOGGER.info("User found : " + foundUser.getFirstName() + " " + foundUser.getLastName());

		return ResponseEntity.ok(foundUser);
	}

	/**
	 * Ajout d'un user dans la persistence
	 * 
	 * @param user
	 *            : user a ajouter
	 * @throws Exception
	 *             Exception
	 */
	@PostMapping("/users")
	public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
		LOGGER.info("New User to create : Id=" + user.getId() + " " + user.getFirstName() + " " + user.getLastName());

		userService.create(user);

		LOGGER.info("User added : Id=" + user.getId() + " " + user.getFirstName() + " " + user.getLastName());

		return ResponseEntity.ok(user);

	}

	/**
	 * Mettre a jour un user dans la persistence
	 * 
	 * @param user
	 *            : le user modifie
	 * @throws Exception
	 *             Exception
	 */
	@PutMapping("/users")
	public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {

		LOGGER.info("User to update : Id=" + user.getId() + " " + user.getFirstName() + " " + user.getLastName());

		userService.update(user);

		LOGGER.info("User updated : Id=" + user.getId() + " " + user.getFirstName() + " " + user.getLastName());

		return ResponseEntity.ok(user);
	}

	/**
	 * Suppression d'un user donne dans persistence
	 * 
	 * @param user
	 *            : le user a supprimer
	 * @throws Exception
	 *             Exception
	 */
	@DeleteMapping("/users/{id}")
	public void supprimerUser(@PathVariable(value = "id") Long userId) {
		LOGGER.info("User to delete : Id=" + userId);

		userService.deleteById(userId);
	}

	/**
	 * Recupere tous les user de la persistence
	 * 
	 * @return : une liste de user
	 * @throws Exception
	 *             Exception
	 */
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> clis = userService.findAll();

		LOGGER.info("Number of Users founded : " + clis.size());

		return ResponseEntity.ok(clis);
	}
}
