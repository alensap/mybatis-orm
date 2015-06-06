# Sample #
http://www.mybatis.org/core/sqlmap-xml.html#Result_Maps

```
// 1안
@Join("Blog LEFT Author LEFT Post LEFT Comment LEFT PostTag LEFT TAG")

// 2안
@Join("Blog LEFT Author ON Blog.authorId = Author.id\
LEFT Post ON Blog.id = Post.blogId\
LEFT Comment ON Post.id = Comment.PostId
LEFT PostTag ON PostTag.postId = Post.id
LEFT TAG ON PostTag.tagId = Tag.id")

class BlogResult {
    @Fields("id,title,authorId")
    Blog blog;
    @Fields("id,username,passwod,email,bio,favouriteSection")
    Author author;
    @Fields("id,blogId,authorId,createdOn,section,subject,draft,body")
    Post post;
    @Fields("id,postId,name,comment")
    Comment comment;
    PostTag postTag;
    @Fields("id,name")
    Tag tag
}
```

```
from Blog B
       left outer join Author A on B.author_id = A.id
       left outer join Post P on B.id = P.blog_id
       left outer join Comment C on P.id = C.post_id
       left outer join Post_Tag PT on PT.post_id = P.id
       left outer join Tag T on PT.tag_id = T.id
```