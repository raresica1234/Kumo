curl -L -X GET "http://localhost:8080/v3/api-docs" --silent > openapi.json

openapi-generator-cli generate -g typescript-angular -i openapi.json -o ../gen --additional-properties ngVersion="17.0.0",debugModels=false,enumPropertyNaming=UPPERCASE,isDateTime=true

rm openapi.json
