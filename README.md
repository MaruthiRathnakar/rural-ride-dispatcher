# Rural Bike Pool

Android-first peer-to-peer bike pool platform for rural and semi-urban routes.

The original repository started with a browser dispatcher prototype. The current product direction is a proper Android app that works more like a rural bike-pooling marketplace:

- Passengers search for rides between villages and towns.
- Verified bike owners post planned rides or accept requests.
- The platform handles trust, matching, trip lifecycle, fare guidance, ratings, and admin operations.

## Product Docs

- [PRD](docs/PRD.md)
- [Architecture Plan](docs/ARCHITECTURE.md)
- [Development Cycle](docs/DEVELOPMENT_CYCLE.md)

## Existing Prototype

The current static dispatcher prototype is still available:

```sh
python3 -m http.server 4173
```

Then visit:

```text
http://127.0.0.1:4173
```

It stores data in the browser using `localStorage`.
