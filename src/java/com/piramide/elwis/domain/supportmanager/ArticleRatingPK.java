package com.piramide.elwis.domain.supportmanager;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 3:00:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArticleRatingPK implements Serializable {

    public Integer userId;
    public Integer articleId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleRatingPK)) {
            return false;
        }

        final ArticleRatingPK articleRatingPK = (ArticleRatingPK) o;

        if (articleId != null ? !articleId.equals(articleRatingPK.articleId) : articleRatingPK.articleId != null) {
            return false;
        }
        if (userId != null ? !userId.equals(articleRatingPK.userId) : articleRatingPK.userId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (userId != null ? userId.hashCode() : 0);
        result = 29 * result + (articleId != null ? articleId.hashCode() : 0);
        return result;
    }
}
