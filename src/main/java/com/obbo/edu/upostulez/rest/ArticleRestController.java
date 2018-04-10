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

import com.obbo.edu.upostulez.domain.Article;
import com.obbo.edu.upostulez.exception.DaoException;
import com.obbo.edu.upostulez.service.IArticleService;

/**
 * Classe qui regroupe tous les traitements concernant un Conseiller courrant. -
 * Ajouter un Article - Recuperer un Article par son ID, lire toutes ces informations
 * (data) - Modifier un Article - Supprimer un Article - Lister tous les Article dans
 * persistence - ToDo : simulationCredit et gestionPatrimoine
 * 
 * DaoArticle est utilise ici pour Chercher ou Modifier l'information dans
 * persistance *
 * 
 * @author JW
 *
 */

@RestController
@RequestMapping("/articles")
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class ArticleRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleRestController.class);

	@Autowired
	private IArticleService articleService;

	public ArticleRestController() {
		super();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Article> findArticleById(@PathVariable(value = "id") Long articleId) throws DaoException {
		LOGGER.info("Find a article by the id : " + articleId);

		Article foundArticle = articleService.findById(articleId)
				.orElseThrow(() -> new DaoException("Article with id : " + articleId + " is not found"));

		LOGGER.info("Article found : " + foundArticle.getName() + " " + foundArticle.getJianTiCi());

		return ResponseEntity.ok(foundArticle);
	}

	/**
	 * Ajout d'un article dans la persistence
	 * 
	 * @param article
	 *            : article a ajouter
	 * @throws Exception
	 *             Exception
	 */
	@PostMapping()
	public ResponseEntity<Article> addArticle(@Valid @RequestBody Article article) {
		LOGGER.info("New Article to create : Id=" + article.getId() + " " + article.getName() + " " + article.getJianTiCi());

		articleService.create(article);

		LOGGER.info("Article added : Id=" + article.getId() + " " + article.getName() + " " + article.getJianTiCi());

		return ResponseEntity.ok(article);

	}

	/**
	 * Mettre a jour un article dans la persistence
	 * 
	 * @param article
	 *            : le article modifie
	 * @throws Exception
	 *             Exception
	 */
	@PutMapping()
	public ResponseEntity<Article> updateArticle(@Valid @RequestBody Article article) {

		LOGGER.info("Article to update : Id=" + article.getId() + " " + article.getName() + " " + article.getJianTiCi());

		articleService.update(article);

		LOGGER.info("Article updated : Id=" + article.getId() + " " + article.getName() + " " + article.getJianTiCi());

		return ResponseEntity.ok(article);
	}

	/**
	 * Suppression d'un article donne dans persistence
	 * 
	 * @param article
	 *            : le article a supprimer
	 * @throws Exception
	 *             Exception
	 */
	@DeleteMapping("/{id}")
	public void supprimerArticle(@PathVariable(value = "id") Long articleId) {
		LOGGER.info("Article to delete : Id=" + articleId);

		articleService.deleteById(articleId);
	}

	/**
	 * Recupere tous les article de la persistence
	 * 
	 * @return : une liste de article
	 * @throws Exception
	 *             Exception
	 */
	@GetMapping()
	public ResponseEntity<List<Article>> getAllArticles() {
		List<Article> clis = articleService.findAll();

		LOGGER.info("Number of Articles founded : " + clis.size());

		return ResponseEntity.ok(clis);
	}
}
