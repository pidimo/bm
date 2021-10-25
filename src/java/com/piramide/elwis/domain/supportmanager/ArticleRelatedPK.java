package com.piramide.elwis.domain.supportmanager;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:33:57 AM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleRelatedPK implements Serializable {

    public Integer articleId;
    public Integer relatedArticleId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleRelatedPK)) {
            return false;
        }

        final ArticleRelatedPK articleRelatedPK = (ArticleRelatedPK) o;

        if (articleId != null ? !articleId.equals(articleRelatedPK.articleId) : articleRelatedPK.articleId != null) {
            return false;
        }
        if (relatedArticleId != null ? !relatedArticleId.equals(articleRelatedPK.relatedArticleId) : articleRelatedPK.relatedArticleId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (articleId != null ? articleId.hashCode() : 0);
        result = 29 * result + (relatedArticleId != null ? relatedArticleId.hashCode() : 0);
        return result;
    }
}
