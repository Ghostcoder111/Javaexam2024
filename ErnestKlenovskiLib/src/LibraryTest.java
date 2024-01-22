import lt.techin.library.BookCatalog;
import lt.techin.library.test.AbstractLibraryTest;

public class LibraryTest extends AbstractLibraryTest {

    @Override
    protected BookCatalog createBookCatalog() {
        return new MyBookCatalog();
    }
}
