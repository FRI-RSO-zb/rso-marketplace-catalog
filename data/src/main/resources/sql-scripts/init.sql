INSERT INTO carbrands(id, name, primaryidentifier, identifiers) VALUES(1, 'Volkswagen', 'vw', 'ww'), (2, 'Audi', 'audi', 'audi'), (3, 'Toyota', 'toyota', 'toyota');

INSERT INTO carmodels(id, name, brandid, primaryidentifier) VALUES(1, 'Golf', 1, 'vw-golf'), (2, 'A4', 2, 'audi-a4'), (3, 'Tiguan', 1, 'vw-tiguan');

INSERT INTO sellers(name, location, contact) VALUES('Seller 1', 'Ljubljana', 'info@seller1.si'), ('Seller 2', 'Kranj', 'info@seller2.si'), ('Seller 3', 'Ljubljana', '/'), ('Seller 4', 'Ljubljana', '(01) 23 45 678');

INSERT INTO ads(source, title, originaluri, sellerid, brandid, modelid) VALUES('marketplace-catalog', 'Testing ad 1, created on marketplace', 'https://catalog.marketplace.bobnar.net/v1/ads/1', 1, 1, 2), ('avtonet', 'Testing ad 2, imported from avtonet', 'https://www.avto.net/Ads/details.asp?id=19043847&display=Suzuki%20Alto', 2, 1, 1), ('bolha', 'Testing ad 3, imported from bolha', 'https://www.bolha.com/avto-oglasi/peugeot-rcz-1.6-thp-147kw-200-km-1-lastnik-oglas-11577078', 3, 3, 2), ('marketplace-catalog', 'Testing ad 4, created on marketplace', 'https://catalog.marketplace.bobnar.net/v1/ads/4', 4, 2, 1);


-- Table carbrands is empty.

-- Table carmodels is empty.

--INSERT INTO sellers(id, contact, location, name) VALUES(1, 'info@seller1.si', 'Ljubljana', 'Seller 1'), (2, 'info@seller2.si', 'Kranj', 'Seller 2'), (3, '/', 'Ljubljana', 'Seller 3'), (4, '(01) 23 45 678', 'Ljubljana', 'Seller 4');

--INSERT INTO ads(id, additionalnotes, bodytype, brand, drivendistancekm, enginetype, firstregistrationmonth, firstregistrationyear, model, originaluri, otherdata, photouri, price, sellerid, source, sourceid, statetype, title, transmissiontype) VALUES(1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'https://catalog.marketplace.bobnar.net/v1/ads/1', NULL, NULL, NULL, 1, 'marketplace-catalog', NULL, NULL, 'Testing ad 1, created on marketplace', NULL), (2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'https://www.avto.net/Ads/details.asp?id=19043847&display=Suzuki%20Alto', NULL, NULL, NULL, 2, 'avtonet', NULL, NULL, 'Testing ad 2, imported from avtonet', NULL), (3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'https://www.bolha.com/avto-oglasi/peugeot-rcz-1.6-thp-147kw-200-km-1-lastnik-oglas-11577078', NULL, NULL, NULL, 3, 'bolha', NULL, NULL, 'Testing ad 3, imported from bolha', NULL), (4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'https://catalog.marketplace.bobnar.net/v1/ads/4', NULL, NULL, NULL, 4, 'marketplace-catalog', NULL, NULL, 'Testing ad 4, created on marketplace', NULL);
