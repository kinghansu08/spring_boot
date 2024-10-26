# spring_boot

6주차까지 실습완료!

추가 구현 할 예정

### article_list.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>블로그 게시판</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h1 class="mt-5">블로그 게시판</h1>

    <!-- 게시글 추가 폼 -->
    <form th:action="@{/api/articles}" method="post">
        <label for="title">제목:</label>
        <input type="text" class="form-control" id="title" name="title" required>
        <div class="form-group">
            <label for="content">내용:</label>
            <textarea class="form-control" id="content" name="content" rows="5" required></textarea>
        </div>
        <button type="submit" class="btn btn-primary">추가</button>
    </form>

    <!-- 게시글 리스트 -->
    <div class="mt-5">
        <h2>게시글 목록</h2>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>제목</th>
                    <th>내용</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="article : ${articles}">
                    <td th:text="${article.id}"></td>
                    <td th:text="${article.title}"></td>
                    <td th:text="${article.content}"></td>
                    <td>
                        <!-- 수정 버튼 -->
                        <a class="btn btn-warning" th:href="@{/article_edit/{id}(id=${article.id})}">수정</a>
                    </td>
                    <td>
                        <!-- 삭제 버튼 -->
                        <form th:action="@{/api/article_delete/{id}(id=${article.id})}" method="post" style="display:inline;">
                            <input type="hidden" name="_method" value="delete">
                            <button type="submit" class="btn btn-danger">삭제</button>
                        </form>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>

### aeticle_edit.html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>블로그 게시판</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h1 class="mt-5">블로그 게시판</h1>

    <!-- 게시글 수정 폼 -->
    <div class="mt-4">
        <h2>게시글을 수정합니다.</h2>
        <form th:action="@{/api/article_edit/{id}(id=${article.id})}" method="post">
            <input type="hidden" name="_method" value="put">
            <div class="form-group">
                <label for="title">제목:</label>
                <input type="text" class="form-control" id="title" name="title" required>
            </div>
            <div class="form-group">
                <label for="content">내용:</label>
                <textarea class="form-control" id="content" name="content" rows="5" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">수정</button>
        </form>
    </div>

    <!-- 게시글 리스트 -->
    <div class="mt-5">
        <h2>게시글 목록</h2>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>제목</th>
                    <th>내용</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="article : ${articles}">
                    <td th:text="${article.id}"></td>
                    <td th:text="${article.title}"></td>
                    <td th:text="${article.content}"></td>
                    <td>
                        <!-- 수정 버튼 -->
                        <a class="btn btn-warning" th:href="@{/article_edit/{id}(id=${article.id})}">수정</a>
                    </td>
                    <td>
                        <!-- 삭제 버튼 -->
                        <form th:action="@{/api/article_delete/{id}(id=${article.id})}" method="post" style="display:inline;">
                            <input type="hidden" name="_method" value="delete">
                            <button type="submit" class="btn btn-danger">삭제</button>
                        </form>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
