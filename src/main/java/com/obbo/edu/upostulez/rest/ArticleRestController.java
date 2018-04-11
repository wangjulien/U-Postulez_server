package com.obbo.edu.upostulez.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.obbo.edu.upostulez.domain.Article;
import com.obbo.edu.upostulez.exception.ResourceNotFoundException;
import com.obbo.edu.upostulez.hateoas.event.PaginatedResultsRetrievedEvent;
import com.obbo.edu.upostulez.hateoas.event.ResourceCreatedEvent;
import com.obbo.edu.upostulez.hateoas.event.SingleResourceRetrievedEvent;
import com.obbo.edu.upostulez.service.IArticleService;

/**
 * Classe qui regroupe tous les traitements concernant un Conseiller courrant. -
 * Ajouter un Article - Recuperer un Article par son ID, lire toutes ces
 * informations (data) - Modifier un Article - Supprimer un Article - Lister
 * tous les Article dans persistence - ToDo : simulationCredit et
 * gestionPatrimoine
 * 
 * DaoArticle est utilise ici pour Chercher ou Modifier l'information dans
 * persistance *
 * 
 * @author JW
 *
 */

@RestController
@RequestMapping("/articles")
@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
public class ArticleRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleRestController.class);

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private IArticleService articleService;

	public ArticleRestController() {
		super();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Article> findArticleById(@PathVariable(value = "id") final Long articleId,
			HttpServletResponse response) {
		LOGGER.info("Find a article by the id : " + articleId);

		Article foundArticle = articleService.findById(articleId)
				.orElseThrow(() -> new ResourceNotFoundException("Article with id : " + articleId + " is not found"));

		eventPublisher.publishEvent(new SingleResourceRetrievedEvent(this, response));
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
	public ResponseEntity<Article> addArticle(@Valid @RequestBody final Article article, HttpServletResponse response) {
		LOGGER.info("New Article to create : Id=" + article.getId() + " " + article.getName() + " "
				+ article.getJianTiCi());

		articleService.create(article);

		eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, article.getId()));
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

		LOGGER.info(
				"Article to update : Id=" + article.getId() + " " + article.getName() + " " + article.getJianTiCi());

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
		List<Article> articles = articleService.findAll();

		LOGGER.info("Number of Articles founded : " + articles.size());

		return ResponseEntity.ok(articles);
	}

	@GetMapping(params = { "page", "size" })
	public ResponseEntity<List<Article>> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {

		Page<Article> resultPage = articleService.findPaginated(page, size);
		if (page > resultPage.getTotalPages()) {
			throw new ResourceNotFoundException();
		}
		eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Article>(Article.class, uriBuilder, response,
				page, resultPage.getTotalPages(), size));

		List<Article> articles = resultPage.getContent();
		LOGGER.info("Number of Articles founded : " + articles.size());
		return ResponseEntity.ok(articles);
	}
}
