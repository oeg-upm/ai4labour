Datos extraídos de la base de datos EMSI/OpenSkills v2.10.1 (usada en otros papers y parece que basada en wikipedia). Más información: https://docs.lightcast.dev/apis/skills ; https://docs.lightcast.dev/apis/skills#versions-version

Las skills pueden visualizarse en web, por ejemplo: https://lightcast.io/open-skills/skills/KS1258263VKQVRQ9N0Y0/networking-hardware
para ver la web a partir del ID, basta https://lightcast.io/open-skills/skills/ + ID


Para obtener ese json, primero hay que autenticarse con las siguiente petición (el client id etc están asociados a mi correo mnavas@fi.upm.es, cualquier problema me decís):
curl --request POST \
  --url https://auth.emsicloud.com/connect/token \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data client_id=wxe5jsngl2udgn0i \
  --data client_secret=3joPFdo3 \
  --data grant_type=client_credentials \
  --data scope=emsi_open
  

Luego se realiza la siguiente petición, añadiendo el token de autenticación recibido en la respuesta de la anterior. Dura una hora.
  
curl --request GET \
  --url 'https://emsiservices.com/skills/versions/latest/skills?q=.NET&typeIds=ST1%2CST2&fields=id%2Cname%2Ctype%2CinfoUrl%2CisSoftware%2Ctags%2CisLanguage%2Cdescription%2CdescriptionSource%2Ccategory%2Csubcategory' \
  --header 'Authorization: Bearer <TOKEN>'
  
Debe quedar algo así:

curl --request GET \
  --url 'https://emsiservices.com/skills/versions/latest/skills?q=.NET&typeIds=ST1%2CST2&fields=id%2Cname%2Ctype%2CinfoUrl%2CisSoftware%2Ctags%2CisLanguage%2Cdescription%2CdescriptionSource%2Ccategory%2Csubcategory' \
  --header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjNDNjZCRjIzMjBGNkY4RDQ2QzJERDhCMjI0MEVGMTFENTZEQkY3MUYiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJQR2FfSXlEMi1OUnNMZGl5SkE3eEhWYmI5eDgifQ.eyJuYmYiOjE2OTExNDEwNTgsImV4cCI6MTY5MTE0NDY1OCwiaXNzIjoiaHR0cHM6Ly9hdXRoLmVtc2ljbG91ZC5jb20iLCJhdWQiOlsiZW1zaV9vcGVuIiwiaHR0cHM6Ly9hdXRoLmVtc2ljbG91ZC5jb20vcmVzb3VyY2VzIl0sImNsaWVudF9pZCI6Ind4ZTVqc25nbDJ1ZGduMGkiLCJlbWFpbCI6Im1uYXZhc0BmaS51cG0uZXMiLCJjb21wYW55IjoiVW5pdiBQb2xpdMOpY25pY2EgZGUgTWFkcmlkIiwibmFtZSI6Ik1hcsOtYSBOYXZhcyIsImlhdCI6MTY5MTE0MTA1OCwic2NvcGUiOlsiZW1zaV9vcGVuIl19.lfoURhgMpA5HPJL8ATnkma0f4g2XwPLqxBzUYC7mhRHoEb4hdnCfjvBnKGjInsgHT_-ymrjYi0LvMftuyBhLCifWcGKtu4-4NW_31DRgPrI0lw4nnjZuH12mIYMbDXB39cWawdCH6PS7MqdZ56-VKzRBff5HKNt0WzO_cK2fXIV8xMN5aWwmqZLnWODGWVsEdeyIRDMP4npUgvzWFOcyxx5_05UB_7SC8NJv9G34IO6GcxVU97thQdsEGKte-QW_K8xIpmxZHtvHBG21MZytwQeH2UJJm5FLWLvHBZFy-fQIQTYcFzUQicdtZ-nJtYKGMMINLxViEafq3QVxiAtoYg'
  
  
  
Los datos por skill son los siguientes:
      "id",
      "type",
      "name",
      "isSoftware",
      "infoUrl",
      "tags",
      "isLanguage",
      "description",
      "descriptionSource",
      "category",
      "subcategory"