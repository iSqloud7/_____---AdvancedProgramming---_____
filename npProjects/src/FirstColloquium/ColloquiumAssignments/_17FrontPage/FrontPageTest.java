package FirstColloquium.ColloquiumAssignments._17FrontPage;

import java.util.*;
import java.util.stream.Collectors;

class TextNewsItem extends NewsItem {

    private String text;

    public TextNewsItem(String title, Date publicationDate, Category category, String text) {
        super(title, publicationDate, category);
        this.text = text;
    }

    /*
       Technology Science Sport
       3
       Booksellers Resisting Amazon’s Disruption
    */
    @Override
    public String getTeaser() {
        // TextNewsItem:getTeaser() - враќа String составен од:
        // -насловот на веста,
        // -пред колку минути е објавена веста (цел број минути) и
        // -максимум 80 знаци од содржината на веста, сите одделени со нов ред.

        long differenceInMills = new Date().getTime() - getPublicationDate().getTime();
        long differenceInMins = differenceInMills / 1000 / 60;

        String teaserText = text.length() < 80 ? text : text.substring(0, 80);

        return String.format("%s\n%d\n%s\n",
                getTitle(), differenceInMins, teaserText);
    }
}

class MediaNewsItem extends NewsItem {

    private String URL;
    private int views;

    public MediaNewsItem(String title, Date publicationDate, Category category, String URL, int views) {
        super(title, publicationDate, category);
        this.URL = URL;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        // MediaNewsItem:getTeaser() - враќа String составен од:
        // -насловот на веста,
        // -пред колку минути е објавена веста (цел број минути),
        // -url-то на веста и
        // -бројот на погледи, сите одделени со нов ред.

        long differenceInMills = new Date().getTime() - getPublicationDate().getTime();
        long differenceInMins = differenceInMills / 1000 / 60;

        return String.format("%s\n%d\n%s\n%d\n",
                getTitle(), differenceInMins, URL, views);
    }
}

class Category {

    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return name;
    }
}

abstract class NewsItem {

    private String title;
    private Date publicationDate;
    private Category category;

    public NewsItem(String title, Date publicationDate, Category category) {
        this.title = title;
        this.publicationDate = publicationDate;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public Category getCategory() {
        return category;
    }

    public abstract String getTeaser();
}

class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}

class FrontPage {

    private List<NewsItem> newsItemList;
    private Category[] categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        this.newsItemList = new ArrayList<>();
    }

    public void addNewsItem(NewsItem newsItem) {
        newsItemList.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category) {
        return newsItemList.stream()
                .filter(cat -> cat.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        for (Category cat : categories) {
            if (cat.getCategoryName().equals(category)) {
                return newsItemList.stream()
                        .filter(c -> c.getCategory().getCategoryName().equals(category))
                        .collect(Collectors.toList());
            }
        }

        // Category Fun was not found
        throw new CategoryNotFoundException(String.format("Category %s was not found", category));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (NewsItem item : newsItemList) {
            builder.append(item.getTeaser());
        }

        return builder.toString();
    }
}

public class FrontPageTest {

    public static void main(String[] args) {

        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}