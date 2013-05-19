Initial Design
===============

### Barcode scanner ###
+ Use android phone to scan a product's barcode to use it in finding information about this product.
+ Using equality search to get the item with the primary key = barcode
	           
### Recommendation of similar products ###
+ Query same category and delta price < threshold
	(delta price = scanned item price - item[i] in same category (same price range) )
+ Range search (where category = product.category AND delta price <= threshold)

### Item catalogue viewer ###
+ View all items on some conditions
+ Items of same category (neat display of local store database range search on this category of items)
           
### History of scanned items ###
+ Saving the current store -using GPS location-
+ Also saving information ( product bought, its price, ... etc)
+ Keep the last 20 scanned items for example

### Favourite list ###
+ Save favorite items on device for offline usage.
+ Insert new entries on device offline DB for later usage.
+ Also can be FK on that entry in the main DB.

### Store location finder ###
+ Search your saved favourite list for the closest store that sells a certain product, or the store that sold you this product the cheapest,.. etc. 
+ Using GPS to locate your position, and using Google Maps to show you location of the saved stores.

### Application interface ###
+ User friendly interface.


Tasks
======

### Nada - Nessma - Nourhan 

#### 1. Map API ####
+ display a map (just a proto type)
+ show markers of locations on the map

#### 2.BarCode Scanner ####
+ display a screen that capture a barcode -prototype
+ translate image to barcode (use a library)

#### 3. Offline on device DB ####
+ Create SQLite DB on device
+ Favoutite and history handelling (insert, retrieve)

### Ashraf - Hazem ###

#### 4. Location ####
+ return current location
+ GPS and Network provided location
+ location tolerance calculation (is in store ?)

#### 5. Online DB MYSQL ####
+ create a new online DB 
+ connect to online DB from device (RESTfull web service + clinet side parsing)


### UI ###
+ all team worked on UI 
+ iconset refer links are to be added soon (some icons may change)
+ all icons and pictures and icons used are opensource.

