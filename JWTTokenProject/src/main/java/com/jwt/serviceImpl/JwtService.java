package com.jwt.serviceImpl;

import java.util.Date;
 

import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.jwt.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final String SECRET_KEY="e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
	
	
	public String generateToken(User user) {
		String token=Jwts
				.builder()
 .subject(user.getUsername())
 .issuedAt(new Date(System.currentTimeMillis()))//when the claim is created
 .expiration(new Date(System.currentTimeMillis()+ 24*60*60*1000))
 .signWith(getSigninKey())
 .compact();//generate and return token
	
		return token;
	}

	//to extract all claims
	private Claims extractAllClaims(String token) {
		return Jwts
				.parser()//converting data from one format to another(parser)
				.verifyWith(getSigninKey()) 
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	private SecretKey getSigninKey() {
		byte[] keyBytes=Decoders.BASE64URL.decode(SECRET_KEY);
		
		// hmacsha(one of the algorithms)
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	//T=type,<T>=generic type(return type)
	//extractclaim is a method is declared as the generic method<T>
	//2 parameters(token[the claim needs to be extracted],resolver[ it is a Function that takes a Claims object (representing the decoded JWT claims) and returns a value of type T. It's used to extract a specific claim from the JWT.]
	public <T>T extractClaim(String token,Function<Claims, T>resolver){
	//method provides a generic way to extract specific claims from a JWT token by applying a resolver function to the decoded claims	
		Claims claims=extractAllClaims(token);//
		return resolver.apply(claims);//used to extracting a specific claim
	}
	
	//Subject should be username/email
	public String extractUsername(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	
	//
	public boolean isValid(String token,UserDetails details) {
		String Username=extractUsername(token);
		return (Username.equals(details.getUsername()))&& !isTokenExpired(token);
	}


	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}


	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	
	
}
