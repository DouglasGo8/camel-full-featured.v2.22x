package com.douglasdb.camel.feat.core.transform.normalizer;

import java.text.SimpleDateFormat;
import java.util.List;

import com.douglasdb.camel.feat.core.domain.csv.BookModel;
import com.douglasdb.camel.feat.core.domain.jaxb.Book;
import com.douglasdb.camel.feat.core.domain.jaxb.Bookstore;

/**
 * 
 * @author Administrador
 *
 */
public class MyNormalizer {

	/**
	 * 
	 * @param books
	 * @return
	 */
	public Bookstore bookModelToJaxb(List<BookModel> books) {
		
		//System.out.println(books.size());

		final Bookstore bookstore = new Bookstore();

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

		for (BookModel bookModel : books) {
			
			final Book book = new Book();

			final Book.Title title = new Book.Title();

			book.setCategory(bookModel.getCategory());

			title.setLang(bookModel.getTitleLanguage());
			title.setValue(bookModel.getTitle());

			book.setTitle(title);

			book.getAuthor().add(bookModel.getAuthor1());

			final String author2 = bookModel.getAuthor2();
			if ((author2 != null) && !author2.isEmpty()) {
				book.getAuthor().add(author2);
			}

			book.setYear(Integer.parseInt(simpleDateFormat.format(bookModel.getPublishDate())));
			book.setPrice(bookModel.getPrice().doubleValue());

			bookstore.getBook().add(book);
		}

		return bookstore;
	}
}
