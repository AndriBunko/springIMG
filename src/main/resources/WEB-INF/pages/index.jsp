<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Prog.kiev.ua</title>
  </head>
  <body>
     <div align="center">
        <form action="/view" method="POST">
            Photo id: <input type="text" name="photo_id">
            <input type="submit" />
        </form>

        <form action="/add_photo" enctype="multipart/form-data" method="POST">
            Photo: <input type="file" name="photo">
            <input type="submit" />
        </form>

         <%--<button type="button" id="delete_photo">Delete</button>--%>

         <table class="table table-striped">
         <thead>
         <tr>
             <td>
             </td>
             <td align="center"><b>Photo ID</b></td>
             <td align="center"><b>Preview</b></td>
         </tr>
         </thead>
         <c:forEach items="${photos_id}" var="photo_id">
             <tr>
                 <td align="center"><input type="checkbox" name="toDelete[]" value="${photo_id}" id="checkbox_${photo_id}"/></td>
                 <td align="center"><a href="/view/${photo_id}" >${photo_id}</a></td>
                 <td><a href="/photo/${photo_id}"><img src="/photo/${photo_id}" height="40"></a></td>
             </tr>
         </c:forEach>
     </table

         <button type="button" id="delete_photo">Delete</button>

         <script >
             $('#delete_photo').click(function(){
             var data = { 'toDelete[]' : []};
             $(":checked").each(function() {
                 data['toDelete[]'].push($(this).val());
             });
             $.post("/delete", data, function(data, status) {
                 window.location.reload();
             });
         });
         </script>
     </div>
  </body>
</html>
