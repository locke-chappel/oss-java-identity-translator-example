## Identity Translator Example
This is an example project that shows a possible solution for converting other Identity Management (IdM) systems into one that is compatible this [Identity API](https://github.com/locke-chappel/oss-commons-api.identity). *This is not a complete project as-is but rather is an example template of what should need to happen.* (Also this means the test coverage is not complete either - it too is largely a functional example and not meant to provide a production ready test suite.)

For example, you may wish to convert Kerberos, SAML, or OAuth solutions to this API. The overall process amounts to receiving the user's credentials (the Identity API is *Same* Sign On, not Single Sign On - user provided credentials are required), forwarding them to the external IdM system, converting the IdM profile into an Identity API JWT, and then passing the JWT back to the caller app.

From the caller app's point of view this translator service is the identity system of record (it will be issuing the JWT and any Identity API calls will be made to this translator service) but the actual data can then either be configured and/or derived by this service or translated and forward to the actual system of record.