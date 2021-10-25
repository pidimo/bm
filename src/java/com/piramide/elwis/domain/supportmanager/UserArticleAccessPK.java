package com.piramide.elwis.domain.supportmanager;

import java.io.Serializable;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public class UserArticleAccessPK implements Serializable {
    public Integer articleId;
    public Integer userGroupId;

    public UserArticleAccessPK() {
    }

    public UserArticleAccessPK(Integer articleId, Integer userGroupId) {
        this.articleId = articleId;
        this.userGroupId = userGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserArticleAccessPK that = (UserArticleAccessPK) o;

        if (articleId != null ? !articleId.equals(that.articleId) : that.articleId != null) {
            return false;
        }
        if (userGroupId != null ? !userGroupId.equals(that.userGroupId) : that.userGroupId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = articleId != null ? articleId.hashCode() : 0;
        result = 31 * result + (userGroupId != null ? userGroupId.hashCode() : 0);
        return result;
    }

}
