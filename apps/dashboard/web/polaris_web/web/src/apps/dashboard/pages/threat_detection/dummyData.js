const loginPayload= {"destIp":null,"method":"POST","requestPayload":"{\"email\":\"victim@gmail.com\",\"password\":\"victim123\"}","responsePayload":"{\"account_id\":55252134, \"authentication\":{\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdGF0dXMiOiJzdWNjZXNzIiwiZGF0YSI6eyJpZCI6MjEsInVzZXJuYW1lIjoidmljdGltIiwiZW1haWwiOiJ2aWN0aW1AZ21haWwuY29tIiwicGFzc3dvcmQiOiJhNjJlN2JlMGE1NjQwODFiNmE5Zjc1MzA4MjA4YzQzMyIsInJvbGUiOiJjdXN0b21lciIsImRlbHV4ZVRva2VuIjoiIiwibGFzdExvZ2luSXAiOiIiLCJwcm9maWxlSW1hZ2UiOiJhc3NldHMvcHVibGljL2ltYWdlcy91cGxvYWRzL2RlZmF1bHQuc3ZnIiwidG90cFNlY3JldCI6IiIsImlzQWN0aXZlIjp0cnVlLCJjcmVhdGVkQXQiOiIyMDIzLTAzLTA5IDE0OjQ2OjI5LjI2OSArMDA6MDAiLCJ1cGRhdGVkQXQiOiIyMDIzLTAzLTA5IDE0OjQ2OjI5LjI2OSArMDA6MDAiLCJkZWxldGVkQXQiOm51bGx9LCJpYXQiOjE2NzgzNzM0MzAsImV4cCI6MTY3ODM5MTQzMH0.JYBu5fv--c9xic_A3yLhvcy2p5o6YjvsVSDnDJ8f5x5cFq5MBfm-Q3a9PrkzFk37QI9nkAsCHXp7lOOdI72sUjHyqZiBu3PT7XzOQmkf8G3D0QZn51oX-bzCEDKbprFoBi5a14duxQvuGhHakoK1La9x8Dgz0SQikeAEsDH6xzo\",\"bid\":6,\"umail\":\"admin@gmail.com\"}}","ip":"null","source":"HAR","type":"HTTP/1.1","akto_vxlan_id":1736491115,"path":"https://juiceshop.akto.io/rest/user/login","requestHeaders":"{\"sec-fetch-mode\":\"cors\",\"referer\":\"https://juiceshop.akto.io/\",\"content-length\":\"51\",\"sec-fetch-site\":\"same-origin\",\"cookie\":\"language=en; welcomebanner_status=dismiss; cookieconsent_status=dismiss; continueCode=v7BmaPZbQ7NroLqvm1YzMVnwOBAVkTefndgpE5jkJlXey43R68K2D9xWNQgq\",\"accept-language\":\"en-GB,en-US;q=0.9,en;q=0.8\",\"origin\":\"https://juiceshop.akto.io\",\"accept\":\"application/json, text/plain, */*\",\"sec-ch-ua\":\"\\\"Not A(Brand\\\";v=\\\"24\\\", \\\"Chromium\\\";v=\\\"110\\\"\",\"sec-ch-ua-mobile\":\"?0\",\"sec-ch-ua-platform\":\"\\\"macOS\\\"\",\"host\":\"juiceshop.akto.io\",\"connection\":\"close\",\"content-type\":\"application/json\",\"accept-encoding\":\"gzip, deflate\",\"user-agent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.5481.178 Safari/537.36\",\"sec-fetch-dest\":\"empty\"}","responseHeaders":"{\"access-control-allow-origin\":\"*\",\"date\":\"Thu, 09 Mar 2023 14:50:30 GMT\",\"content-length\":\"826\",\"x-content-type-options\":\"nosniff\",\"x-recruiting\":\"/#/jobs\",\"vary\":\"Accept-Encoding\",\"x-frame-options\":\"SAMEORIGIN\",\"etag\":\"W/\\\"33a-CNP0uzn0+OgghHzsSNi62nRjeIw\\\"\",\"connection\":\"close\",\"content-type\":\"application/json; charset=utf-8\",\"feature-policy\":\"payment 'self'\"}","time":"1736491115","statusCode":"200","status":"OK","akto_account_id":"1000000","direction":null,"is_pending":"false"}
const accountPayload = {"destIp":null,"method":"POST","requestPayload":"{\"isAdmin\":true}","responsePayload":"{\"accounts\":[{\"accountId\":\"acc-20231201-123456\",\"owner\":\"John Smith\",\"ssn\":\"123-45-6789\",\"address\":{\"street\":\"123 Elm St\",\"city\":\"Springfield\",\"state\":\"IL\",\"zip\":\"62704\"},\"phoneNumber\":\"+1-217-555-0123\",\"email\":\"john.smith@example.com\",\"balance\":250000,\"portfolioId\":\"PORT-20231201-654321\",\"riskRating\":3,\"managementFee\":1.2,\"accounts\":[{\"accountId\":\"sub-20231201-654321\",\"type\":\"retirement\",\"balance\":150000,\"investmentReturns\":15},{\"accountId\":\"sub-20231201-987654\",\"type\":\"savings\",\"balance\":100000,\"investmentReturns\":5}]}]}","ip":"null","source":"HAR","type":"HTTP/1.1","akto_vxlan_id":1738051842,"path":"https://api.investmentservices.bankone.com/investments/accounts","requestHeaders":"{\"authorization\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ\",\"content-type\":\"application/json\",\"accept\":\"application/json\"}","responseHeaders":"{\"content-type\":\"application/json\"}","time":"1738051936","statusCode":"200","status":"OK","akto_account_id":"1669322524","direction":null,"is_pending":"false"}
const reportPayload = {"destIp":null,"method":"GET","requestPayload":"{}","responsePayload":"{\"ownerId\":\"william@bankone.com\", \"reports\":[{\"id\":\"REPORT-20231201-123456\",\"title\":\"Q4 Market Outlook\",\"publishedDate\":\"2023-12-01\",\"author\":\"John Smith\",\"summary\":\"An in-depth analysis of the Q4 market trends and forecasts.\",\"keyFindings\":[\"Tech stocks are projected to see a significant rise.\",\"Real estate market is expected to stabilize.\",\"Renewable energy investments will continue to grow.\"]},{\"id\":\"REPORT-20231202-654321\",\"title\":\"Investment Strategies for 2024\",\"publishedDate\":\"2023-12-02\",\"author\":\"Sarah Johnson\",\"summary\":\"Strategies investors might consider for the coming year.\",\"keyFindings\":[\"Diversification across technology and healthcare sectors is recommended.\",\"Emerging markets are becoming increasingly attractive.\",\"Long-term investments in AI and blockchain technologies appear promising.\"]}]}","ip":"null","source":"HAR","type":"HTTP/1.1","akto_vxlan_id":1738051842,"path":"https://api.investmentservices.bankone.com/investments/research/reports","requestHeaders":"{\"authorization\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ\",\"content-type\":\"application/json\",\"accept\":\"application/json\"}","responseHeaders":"{\"content-type\":\"application/json\"}","time":"1738051936","statusCode":"200","status":"OK","akto_account_id":"1669322524","direction":null,"is_pending":"false"}
const tradePayload = {"destIp":null,"method":"POST","requestPayload":"{\"accountId\":\"12345678901\",\"portfolioId\":\"PORT-20231201-654321\",\"symbol\":\"AAPL\",\"quantity\":100,\"price\":150.0,\"tradeType\":\"buy\",\"orderType\":\"market\"}","responsePayload":"{\"user_id\":754342563,\"tradeId\":\"TRADE-20231201-654321\",\"accountId\":\"12345678901\",\"portfolioId\":\"PORT-20231201-654321\",\"symbol\":\"AAPL\",\"quantity\":100,\"executedPrice\":150.0,\"status\":\"executed\",\"transactionFee\":25.0,\"dateTime\":\"2023-12-01T12:00:00Z\"}","ip":"null","source":"HAR","type":"HTTP/1.1","akto_vxlan_id":1739354553,"path":"https://api.investmentservices.bankone.com/investments/trades","requestHeaders":"{\"authorization\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ\",\"content-type\":\"application/json\",\"accept\":\"application/json\"}","responseHeaders":"{\"content-type\":\"application/json\"}","time":"1739355772","statusCode":"200","status":"OK","akto_account_id":"1000000","direction":null,"is_pending":"false"}
const calendarPayload = {"destIp":null,"method":"GET","requestPayload":"{}","responsePayload":"{\"marketDays\":[{\"date\":\"2023-12-01\",\"status\":\"open\",\"openingBell\":\"09:30\",\"closingBell\":\"16:00\"},{\"date\":\"2023-12-02\",\"status\":\"closed\",\"reason\":\"Weekend\"},{\"date\":\"2023-12-03\",\"status\":\"closed\",\"reason\":\"Weekend\"},{\"date\":\"2023-12-04\",\"status\":\"open\",\"openingBell\":\"09:30\",\"closingBell\":\"16:00\"}]}","ip":"null","source":"HAR","type":"HTTP/1.1","akto_vxlan_id":1738051842,"path":"http://api.investmentservices.bankone.com/investments/market/calendar?=http://attacker.com/maliciousPage","requestHeaders":"{\"authorization\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ\",\"content-type\":\"application/json\",\"accept\":\"application/json\"}","responseHeaders":"{\"content-type\":\"application/json\"}","time":"1738051936","statusCode":"200","status":"OK","akto_account_id":"1669322524","direction":null,"is_pending":"false"}
const AAPLPayload = {"destIp":null,"method":"GET","requestPayload":"{}","responsePayload":"{\"symbol\":\"AAPL\",\"companyName\":\"Apple Inc.\",\"stockPrice\":150.25,\"investmentReturn\":0.05,\"riskRating\":2,\"portfolioId\":\"PORT-20230910-349857\",\"currentValue\":50000.0,\"returns\":{\"1Y\":0.25,\"3Y\":0.55,\"5Y\":0.8},\"transactionFees\":25.0,\"managementFees\":1.5,\"tradeId\":\"TRADE-20231201-239487\",\"status\":\"executed\"}","ip":"null","source":"HAR","type":"HTTP/1.1","akto_vxlan_id":1739354553,"path":"https://api.investmentservices.bankone.com/investments/stocks/AAPL?userId=' OR 1=1 -- - 432454","requestHeaders":"{\"authorization\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ\",\"content-type\":\"application/json\",\"accept\":\"application/json\"}","responseHeaders":"{\"content-type\":\"application/json\"}","time":"1739355772","statusCode":"200","status":"OK","akto_account_id":"1000000","direction":null,"is_pending":"false"}
const indicesPayload = {"destIp":null,"method":"GET","requestPayload":"{\"apiKey\":\"wyugqeyuh238478r37dvqugx182328r7gec\"}","responsePayload":"{\"indices\":[{\"name\":\"Dow Jones Industrial Average\",\"symbol\":\"DJIA\",\"currentValue\":33589.12,\"change\":\"-0.23%\"},{\"name\":\"NASDAQ Composite\",\"symbol\":\"IXIC\",\"currentValue\":12056.44,\"change\":\"0.52%\"},{\"name\":\"S&P 500\",\"symbol\":\"SPX\",\"currentValue\":4112.58,\"change\":\"0.12%\"}]}","ip":"null","source":"HAR","type":"HTTP/1.1","akto_vxlan_id":1738051842,"path":"https://api.investmentservices.bankone.com/investments/market/indices","requestHeaders":"{\"authorization\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ\",\"content-type\":\"application/json\",\"accept\":\"application/json\"}","responseHeaders":"{\"content-type\":\"application/json\"}","time":"1738051936","statusCode":"200","status":"OK","akto_account_id":"1669322524","direction":null,"is_pending":"false"}
const xxPayload = {"destIp":null,"method":"GET","requestPayload":"{\"name\":\"John's Investment Strategies\", \"ownerId\":\"1234567890\"}","responsePayload":"{\"error processing request\"}]}","ip":"null","source":"HAR","type":"HTTP/1.1","akto_vxlan_id":1738051842,"path":"https://api.investmentservices.bankone.com/investments/watchlists","requestHeaders":"{\"authorization\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ\",\"content-type\":\"application/json\",\"accept\":\"application/json\"}","responseHeaders":"{\"content-type\":\"application/json\"}","time":"1738051936","statusCode":"200","status":"OK","akto_account_id":"1669322524","direction":null,"is_pending":"false"}

const sampleDataMap = {
  "investments/market/indices": indicesPayload,
  "investments/market/calendar": calendarPayload,
  "investments/stocks/AAPL": AAPLPayload,
  "investments/research/reports": reportPayload,
  "investments/trades": tradePayload,
  "investments/accounts": accountPayload,
  "rest/user/login": loginPayload,
  "investments/watchlists": xxPayload
}

const tempFunc = {
  getSampleDataOfUrl: (url) => {
    let key = url.split(" ");
    if(key.length > 1){
      key = key[1];
    }else{
      key = key[0];
    }
    const sampleKey =  key.startsWith("/") ? key.slice(1) : key;
    return sampleDataMap[sampleKey];
  },
}

export default tempFunc;