INSERT INTO sellers(name, location, contact) VALUES('Seller 1', 'Ljubljana', 'info@seller1.si'), ('Seller 2', 'Kranj', 'info@seller2.si'), ('Seller 3', 'Ljubljana', '/'), ('Seller 4', 'Ljubljana', '(01) 23 45 678');

INSERT INTO ads(source, title, originaluri, sellerid) VALUES('marketplace-catalog', 'Testing ad 1, created on marketplace', 'https://catalog.marketplace.bobnar.net/v1/ads/1', 1), ('avtonet', 'Testing ad 2, imported from avtonet', 'https://www.avto.net/Ads/details.asp?id=19043847&display=Suzuki%20Alto', 2), ('bolha', 'Testing ad 3, imported from bolha', 'https://www.bolha.com/avto-oglasi/peugeot-rcz-1.6-thp-147kw-200-km-1-lastnik-oglas-11577078', 3), ('marketplace-catalog', 'Testing ad 4, created on marketplace', 'https://catalog.marketplace.bobnar.net/v1/ads/4', 4);
